# blotor
a blog generator

# how to
## initialize blog
```
./gradlew run -Pargs=initialize
```

## generate new article
```
./gradlew run -Pargs=generate
```

## build blog
```
./gradlew run -Pargs=build
```

# theme
## 404.html
### arguments
```
recentArticleList: List<Article>
tagList: List<Tag>
```

## article.html
### arguments
```
article: Article
recentArticleList: List<Article>
tagList: List<Tag>
```

## article-list.html
### arguments
```
articleList: List<Article>
currentPage: Int
lastPage: Int
pageUrlPrefix: String
recentArticleList: List<Article>
tagList: List<Tag>
```
