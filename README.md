# React Hooks

# 1. フックの導入

フックとはステートフルなロジックを共有するための基本機能。ステートをもつロジックを抽出して単独でテストしたり再利用したりすることができる。コンポーネントの階層構造を変えずに再利用できることがポイント。

# 2. フック早わかり

## useState

### useStateの引数

`useState`の引数はステートの初期値。最初のレンダー時にのみ使用される。

### useStateの戻り値

`useState`は現在のステートの値とそれを更新するための関数をペアにして返す。これを配列の分割代入構文で受け取ることができる。

## useEffect

副作用のためのフック。`componentDidMount`, `componentDidUpdate`, `componentWillUnmount`と同様の目的で使うものだが、ひとつのAPIに統合されている。

DOMへの更新を反映した後に`useEffect`で定義する副作用関数が実行される。関数を返却するとこれをクリーンアップとして指定できる。クリーンアップはコンポーネントがアンマウントされる時や副作用が再実行される時に実行される。

## フックのルール

- フックは関数のトップレベルでのみ呼び出す
- フックはReactの関数コンポーネントの内部でのみ呼び出す

## 独自フック

独自フックはステートを用いたロジックを再利用する。ステートそのものを再利用するのではない。従って、フックのそれぞれの呼び出しが独立したステートを持つ。

# 3. ステートフックの利用法

関数コンポーネントはステートレスコンポーネントでなくなる。フックがステートを利用できるようにするから。フックはクラスを書く代わりに使う。

関数コンポーネント内で`useState`を呼び出すことでステート変数を宣言する。戻り値は値と値を更新するための関数。分割代入でこれらを受け取ることができる。

## 所感

値と値を更新するための関数を**用途ごとで一緒**にできることがポイントか。

# 4. 副作用フックの利用法

データの取得/購読やDOMの手動変更などを副作用 (side-effect) あるいは単に作用 (effect) と呼ぶ。

副作用フック`useEffect`はReactライフサイクルの`componentDidMount`と`componentDidUpdate`と`componentWillUnmount`がまとまったもの。

## クリーンアップを必要としない副作用

`useEffect`はマウント後と更新後に呼ばれる。つまりレンダーの後に副作用は起こる。副作用が実行される時点でDOMが正しく更新され終わっていることを保証する。

Reactは再レンダーごとに違う副作用関数スケジュールする。副作用は特定の1回のレンダーと結びつく。なお`useEffect`は`componentDidMount`や`componentDidUpdate`と異なりブラウザによる画面更新をブロックしない。同期的に行う必要がある場合`useLayoutEffect`を利用する。

## クリーンアップを有する副作用

`props`で受け取る値を利用するとき、場合によって`componentDidUpdate`でクリーンアップする必要がある。この処理のし忘れがバグの原因になる。

副作用フックでは`useEffect`の引数にする関数の戻り値でクリーンアップを定義することができる。

### 副作用のスキップによるパフォーマンス改善

副作用のクリーンアップと適用をレンダーごとに繰り返すのはパフォーマンスの面で具合が悪い。これを回避するために、クラスでは`componentDidUpdate`の引数`prevProps`や`prevState`を利用して今回の値と前回の値を比較することができる。

副作用フックでは`useEffect`のふたつめの引数で比較対象を指定することができる。

## 所感

副作用のクリーンアップと適用を**ひとつの定義**にできることがやはりポイント。またこのことでマウント後と更新後を**一緒にしてレンダー後**としてしまえる。関心とコードをよりシームレスにできる気がする。