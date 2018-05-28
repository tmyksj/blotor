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
## global attribute list
```
ts: TemplateSupport
```

## 404.html
### local attribute list
```
(none)
```

## article.html
### local attribute list
```
article: Article
```

## article-list.html
### local attribute list
```
articleList: List<Article>
currentPage: Int
lastPage: Int
pageUrlPrefix: String
```
