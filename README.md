# Kotlin Movie App

TMDb APIを使った映画検索アプリ - ネットワーク通信の学習プロジェクト

## 概要
Retrofit、Coil、Paging 3などのライブラリを使用して、モダンなネットワーク通信を学習するプロジェクトです。

## 機能
- [ ] 人気映画一覧
- [ ] 高評価映画一覧
- [ ] 公開予定映画一覧
- [ ] 映画検索
- [ ] 映画詳細表示
- [ ] お気に入り機能
- [ ] オフライン対応（キャッシング）

## 技術スタック
- Jetpack Compose
- Retrofit（ネットワーク通信）
- Coil（画像読み込み）
- Paging 3（ページング）
- Room Database（キャッシング）
- Hilt（DI）
- MVVM アーキテクチャ

## API
- TMDb API (The Movie Database)

## セットアップ

### API Keyの取得
1. https://www.themoviedb.org/ でアカウント作成
2. Settings → API → Request API Key
3. API Key (v3) を取得

### プロジェクトの設定
1. プロジェクトルートの `local.properties` に以下を追加：
```
TMDB_API_KEY=YOUR_API_KEY_HERE
```

## 学習目標
- Retrofitでのネットワーク通信
- JSONシリアライゼーション
- 画像読み込みとキャッシング
- エラーハンドリング
- リトライロジック
- ページング
- オフライン対応
```