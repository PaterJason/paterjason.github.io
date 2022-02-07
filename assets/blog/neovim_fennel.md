# Clojure and Neovim

## Powering Lisp editing with Lisp

The release of Neovim 0.5 brings improved Lua support, a built-in LSP client
and other features such as Treesitter. We can use these improvements, with the
plugins they enable, to enhance both the Clojure editing and editor
configuration experience.

### Vimscript is dead. And we have killed it

Beyond simple things like setting options and variables, I find Vimscript a
complete pain to write. Writing plugins and configurations in other languages
is possible using providers such as with the Python or Node clients. Now, with
the embedded integration of LuaJIT and improvements to the Neovim
[API](https://neovim.io/doc/user/api.html), we can almost completely ditch
Vimscript without the need for external dependencies.

So life is good, but it can be better. Given that we will be editing Clojure we
may be more comfortable using a Lisp for our configuration. Instead of writing
Lua directly, we can write [Fennel](https://fennel-lang.org/), a Lisp that
compiles to Lua. Using the plugin [Aniseed](https://github.com/Olical/aniseed)
we can write our configuration using a Language with a syntax and macro system
familiar to Clojure users.

We can bootstrap our configuration with Aniseed and a [plugin
manager](https://github.com/wbthomason/packer.nvim) like so

`~/.config/nvim/init.lua`

```lua
local function bootstrap(user, repo)
  local url = 'https://github.com/'..user..'/'..repo
  local install_path = vim.fn.stdpath('data')..'/site/pack/packer/start/'..repo
  if vim.fn.empty(vim.fn.glob(install_path)) > 0 then
    vim.fn.system({'git', 'clone', '--depth', '1', url, install_path})
  end
end
bootstrap('wbthomason', 'packer.nvim')
bootstrap('Olical', 'aniseed')
vim.g['aniseed#env'] = true
```

Aniseed will compile our Fennel configuration under `fnl/` into Lua and run
`fnl/init.fnl`.  The rest of our configuration can be done in Fennel. For
example, we can define the plugins we shall be using:


`~/.config/nvim/fnl/init.fnl`

```fennel
(module init
  {autoload {packer packer}})

(def- packages
  {"Olical/aniseed" {}
   "wbthomason/packer.nvim" {}

   ; Completion
   "hrsh7th/nvim-cmp"
   {:requires ["PaterJason/cmp-conjure" "hrsh7th/cmp-nvim-lsp"]
    :config (fn []
              (let [cmp (require :cmp)]
                (cmp.setup
                  {:sources [{:name "conjure"}
                             {:name "nvim_lsp"}]})))}

   ; Language Server Protocol
   "neovim/nvim-lspconfig" {:config (fn [] (require :lspcfg))}

   ; Conjure
   "Olical/conjure"
   {:config (fn []
              (set vim.g.conjure#mapping#doc_word "K")
              (set vim.g.conjure#client#fennel#aniseed#aniseed_module_prefix "aniseed."))}

   "clojure-vim/vim-jack-in" {}})

(each [name cfg (pairs packages)]
  (tset cfg 1 name))

(packer.startup {1 (vim.tbl_values packages)
                 :config {}})
```

### Conjure

A means of interacting with a REPL is essential to almost any Clojure workflow.
My nREPL client of choice is [Conjure](https://github.com/Olical/conjure). It
provides a pleasant interface with its floating window "HUD" along with the
features you would expect from an nREPL client:

- Automatically connect to a running CIDER nREPL
- Evaluate code from the buffer
- Run tests
- Go to definition, documentation
- Autocompletion


What’s more is Fennel with Aniseed is also supported (along with other Lisps),
meaning we can use the same REPL workflow when configuring Neovim.

### Language Server

By using a language server our environment will start looking more like an IDE.
[clojure-lsp](https://clojure-lsp.io/) through static analysis gives us a range
of tools, such as

- Autocompletion
- Renaming
- Go to definition
- Find references
- Snippets
- [clj-kondo](https://github.com/clj-kondo/clj-kondo) powered diagnostics
- Code actions
- Code lens
- clj-refactor like refactoring

With Neovim 0.5 we can use the built-in LSP client. The base configuration for
the Clojure LSP (as well as many others) is provided by
[nvim-lspconfig](https://github.com/neovim/nvim-lspconfig). With the
`clojure-lsp` executable on our `PATH` we can configure our LSP client like so:

`~/.config/nvim/fnl/lspcfg.fnl`

```fennel
(module lspcfg
  {autoload {lsp lspconfig
             cmp_lsp cmp_nvim_lsp}})

(def- keymap-opts {:noremap true})

; Function for clojure lsp refactoring
(global clj_lsp_cmd
  (fn [cmd prompt]
    (let [params (vim.lsp.util.make_position_params)
          args [params.textDocument.uri params.position.line params.position.character]]
      (when prompt
        (table.insert args (vim.fn.input prompt)))
      (vim.lsp.buf.execute_command {:command cmd
                                    :arguments args}))))

(defn- map-clj [bufnr]
  (each [k v (pairs {:cc "'cycle-coll'"
                     :th "'thread-first'"
                     :tt "'thread-last'"
                     :tf "'thread-first-all'"
                     :tl "'thread-last-all'"
                     :uw "'unwind-thread'"
                     :ua "'unwind-all'"
                     :ml "'move-to-let', 'Binding name: '"
                     :il "'introduce-let', 'Binding name: '"
                     :el "'expand-let'"
                     :am "'add-missing-libspec'"
                     :cn "'clean-ns'"
                     :cp "'cycle-privacy'"
                     :is "'inline-symbol'"
                     :ef "'extract-function', 'Function name: '"
                     :ai "'add-import-to-namespace', 'Import name: '"})]
    (vim.api.nvim_buf_set_keymap
      bufnr :n (.. "<leader>r" k) (.. "<cmd>call v:lua.clj_lsp_cmd(" v ")<CR>") keymap-opts)))

; Run when the Language Server attaches to the buffer
(defn- on-attach [client bufnr]
  ; Set keymaps and other LSP configuration here
  (vim.api.nvim_buf_set_keymap bufnr "n" "K" "<cmd>lua vim.lsp.buf.hover()<CR>" keymap-opts)
  (vim.api.nvim_buf_set_keymap bufnr "n" "gd" "<cmd>lua vim.lsp.buf.definition()<CR>" keymap-opts))

(def- capabilities
  (cmp_lsp.update_capabilities (vim.lsp.protocol.make_client_capabilities)))

(lsp.clojure_lsp.setup
  {:on_attach (fn [client bufnr]
                (map-clj bufnr)
                (on-attach client bufnr))
   :capabilities capabilities})

```

### Other tools

#### Completion

I use [nvim-cmp](https://github.com/hrsh7th/nvim-cmp) as my completion engine.
LSP capabilities, such as snippets, are well supported. For autocompletion from
Conjure I’ve published my cmp source configuration as a
[plugin](https://github.com/PaterJason/cmp-conjure)

#### Editing S-expressions

[vim-sexp](https://github.com/guns/vim-sexp) with [tpope’s
mappings](https://github.com/tpope/vim-sexp-mappings-for-regular-people)
remains my preferred way of editing S-expressions.

If you’re more of a fan of Parinfer, there is
[nvim-parinfer](https://github.com/gpanders/nvim-parinfer) which uses a Lua
implementation of the Parinfer algorithm.


#### Telescope

For my fuzzy finding needs I use
[Telescope](https://github.com/nvim-telescope/telescope.nvim). It also includes
pickers for the LSP, accelerating my navigation through a codebase.



#### Treesitter

[Treesitter](https://tree-sitter.github.io/tree-sitter) is a parsing tool, that
can quickly build a syntax tree as you type. Neovim 0.5 introduced initial
support for Treesitter. Using the available Clojure and Fennel grammars we can
enhance syntax highlighting and text objects.


### Wrap up

I provided some basic Fennel configuration here to get started. For a more
in-depth config, my personal
[dotfiles](https://github.com/PaterJason/dotfiles/tree/master/stow/nvim/.config/nvim)
are publicly available. And feel free to ping me on `#vim` in the [Clojurians
Slack](http://clojurians.net/) if you have any questions.
