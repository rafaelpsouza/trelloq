Trelloq
=============

* Query your trello board like a relational database

## Compile and create dist file
```sbt compile dist```

## Generating trello credentials
https://trello.com/1/appKey/generate

## Request a token granting read-only access forever:
```
https://trello.com/1/authorize?key=substitutewithyourapplicationkey&name=My+Application&expiration=never&response_type=token
```


### Examples


#### Count cards

```./trelloq mytoken mykey myboardId "select count(*) from card" ```

#### Display list ids and names

```./trelloq mytoken mykey myboardId "select id, name from list" ```

#### Count card with BUG label created after some day

``` ./trelloq mytoken mykey myboardId "select count(*) from card as card
      join card_label as card_label on card.id = card_label.idCard
      join label as label on label.id = card_label.idLabel where label.name = 'BUG' and card.created > '2017-12-24'" ```