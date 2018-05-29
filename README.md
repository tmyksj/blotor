# blotor
a blog generator

# how to
## install blotor and initialize blog
```
# clone this repository
git clone git@github.com:tmyksj/blotor.git

# change directory
cd blotor

# install blotor to your blog repository (or directory)
./gradlew run -Pargs="install [install-path]"

# change directory
cd [install-path]

# initialize blog
./gradlew run -Pargs=initialize
```

## add new article and build
```
# generate article source
./gradlew run -Pargs=generate

# write your article to
#   article/{uuid}/index.html

# build blog
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
