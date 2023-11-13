# icf-tools

convert between icf and json

```json
{
  "appID": "SSSS",
  "platformID": "BDB",
  "platformGeneration": "0",
  "contents": [
    {
      "type": "PLATFORM",
      "version": "99.99.99",
      "buildDate": "19190810114514",
      "requiredPlatformVersion": "99.99.99"
    },
    {
      "type": "APP",
      "version": "9.97.00",
      "buildDate": "19190810114514",
      "requiredPlatformVersion": "99.99.99"
    },
    {
      "type": "PATCH",
      "version": "9.98.00",
      "buildDate": "19190810114514",
      "requiredPlatformVersion": "99.99.99"
    },
    {
      "type": "PATCH",
      "version": "9.99.00",
      "buildDate": "19190810114514",
      "requiredPlatformVersion": "99.99.99"
    },
    {
      "type": "OPTION",
      "version": "A114",
      "buildDate": "19190810114514",
      "requiredPlatformVersion": "00.00.0"
    }
  ]
}
```

json to icf
```bash
java -jar icf-tools.jar j2i <jsonFile>
```

icf to json
```bash
java -jar icf-tools.jar i2j <icfFile>
```