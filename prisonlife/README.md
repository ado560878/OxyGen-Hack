# PrisonLife Plugin (Paper 1.21.4)

Roblox Prison Life'tan esinlenmis hapishane roleplay plugini.

## Ozellikler
- `/pb join` — Takim secme GUI'si (Mahkum Katil / Gardiyan Katil)
- Ayni takima tekrar katilmaya calisinca "Zaten bu takimdasin!" uyarisi
- SkinsRestorer ile otomatik skin: mahkum → `Prisonnier`, gardiyan → `PoliceOfficer_` (config.yml'den degistirilebilir)
- Gardiyan kiti: Gardiyan Kilici (tahta kilic), Kelepce (cubuk), Sok Cihazi (blaze cubugu)
- **Kelepce**: Mahkuma sag tik → 3 saniye hareket edemez/vuramaz, sonra mahkum spawn'ina isinlanir
- **Sok Cihazi**: Mahkuma sag tik → 5 saniye mide bulantisi + Adventure moduna gecer, sonra eski moduna doner
- `/setspawn mahkum` ve `/setspawn gardiyan` ile spawn noktalari (OP)
- Takim ici vurma engeli (mahkum-mahkum, gardiyan-gardiyan)
- Prefix: `&7[&6PrisonLife&7]`

## Kurulum
1. Sunucuya [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/) kurun (skinler icin)
2. `PrisonLife-1.0.0-obf.jar` dosyasini `plugins/` klasorune atin
3. Sunucuyu baslatin, `plugins/PrisonLife/config.yml` uzerinden ayarlari duzenleyin
4. `/setspawn mahkum` ve `/setspawn gardiyan` ile spawn noktalarini belirleyin

## Derleme
JDK 21 gerekir:
```bash
cd prisonlife
mvn package
```
- `target/PrisonLife-1.0.0-obf.jar` → **obfuscate edilmis (kodlar gizli)** surum, sunucuda bunu kullanin
- `target/PrisonLife-1.0.0.jar` → normal surum

Obfuscation ProGuard ile yapilir; sadece `config.yml` ve `plugin.yml` okunabilir kalir, kodlar gizlidir.
