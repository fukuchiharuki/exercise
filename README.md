# 手を動かしながら2週間で学ぶAWS基本から応用まで

1. [Day 1. AWSことはじめ](./Day-1.AWSことはじめ.md)
   - AWSアカウントの作成
   - AWSアカウントの作成後にやるべきこと
   - 料金の見積もり
1. [Day 2. EC2インスタンスを起動してみる](./Day-2.EC2インスタンスを起動してみる.md)
   - EC2インスタンスの起動
   - ミドルウェア（httpd）の導入
   - AMIの作成、EC2インスタンスの再作成
   - EIPでIPアドレスを固定化
1. [Day 3. AWSにおけるネットワーク(1)](./Day-3.AWSにおけるネットワーク(1).md)
   - VPCの作成
   - サブネットの作成、ルートテーブルの変更
   - OSの初期設定
     - ホスト名の変更
     - タイムゾーンの変更
   - ミドルウェア（httpd24, php70-*）の導入・初期設定
1. [Day 4. AWSにおけるネットワーク(2)](./Day-4.AWSにおけるネットワーク(2).md)
   - プライベートサブネットの作成
     - NAT ゲートウェイの作成・利用
   - セキュリティグループを指定するセキュリティグループ
1. [Day 5. RDBのマネージドサービスRDSを使う](./Day-5.RDBのマネージドサービスRDSを使う.md)
   - Master-Slave構成なRDSの作成
     - サブネットグループ
     - マルチアベイラビリティゾーン
   - バックアップ（スナップショット）とリストア（復元）
1. [Day 6. ELBを用いてWebレイヤの可用性を高める](./Day-6.ELBを用いてWebレイヤの可用性を高める.md)
   - ELB (ALB) の作成
     - セキュリティグループ
     - ターゲットグループ
   - Auto Scaling
     - 起動設定
     - Auto Scaling グループ
1. [Day 7. オブジェクトストレージS3を使ってみる](./Day-7.オブジェクトストレージS3を使ってみる.md)
   - バケットの作成
   - アプリケーションからの利用
   - 静的ホスティング
1. [Day 8. Route 53を使ってドメイン登録する](./Day-8.Route53を使ってドメイン登録する.md)
   - NS設定
   - シンプルなルーティング設定
   - Failoverルーティング設定