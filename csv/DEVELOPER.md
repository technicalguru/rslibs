# Developer Remarks

## Dependency Management
Automatic dependency upgrade check is performed in validate phase. Hence, a current upgrade candidate list
is always available during build.

### List Latest Dependency Releases
```
mvn versions:display-dependency-updates
```

### Upgrade to Newest Dependency Releases
```
mvn versions:use-latest-releases
```

This will also create a backup of pom.xml at ``pom.xml.versionsBackup``.

### Confirm Dependency Upgrades:
```
mvn versions:commit
```

### Rollback Dependency Upgrades
```
mvn versions:revert
```
