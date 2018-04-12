# Konfo-files

Uuden koulutusinformaation (Konfo-UI) tiedostoja jakava sovellus.

## Vaatimukset

Sovelluksen ajaminen lokaalisti ei ole tällä hetkellä mahdollista. 

S3-toiminnallisuuksia voidaan testata mock-testeillä, jotka pystyy ajamaan komennolla:

`lein test`

## Lokaali ajo 

*Huom!* Vaatii s3 bucketin ja sovelluksen ajamisen AWS:ssä autentikointia varten! Ei toimi tällä hetkellä!

Lokaalia ajoa varten kopioi konfiguraatiotiedoston template `dev-configuration/konfo-backend.end.template`
tiedostoksi `dev-configuration/konfo-backend.edn` ja lisää tiedostoon oikeat arvot:

```
{
 :s3-bucket "opintopolku-hahtuva-koulutusinformaatio"
 :s3-region "eu-west-1"
}
```

Sovelluksen voi käynnistää komennolla:

`lein run`