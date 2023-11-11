# icfgenerator


convert json to `SSSS` icf file

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
      "requiredPlatformVersion": "99.99.99"
    }
  ]
}
```

save, and execute command
```bash
java -jar icfgenerator.jar -f <fileName>.json
```

The output will be
```bash
<fileName>.json.icf
```