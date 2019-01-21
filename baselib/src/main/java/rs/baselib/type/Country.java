/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.type;

/**
 * Countries on this planet.
 * @author ralph
 *
 */
public enum Country {

	AFGHANISTAN(Continent.ASIA, "Kabul","AF", "AFG", "004", "+93", ".af"),
	ALBANIA(Continent.EUROPE, "Tirana","AL", "ALB", "008", "+355", ".al"),
	ALGERIA(Continent.AFRICA, "Algier","DZ", "DZA", "012", "+213", ".dz"),
	AMERICAN_SAMOA(Continent.AUSTRALIA, "Pago-Pago","AS", "ASM", "882", "+1684", ".as"),
	ANDORRA(Continent.EUROPE, "Andorra la Vella","AD", "AND", null, "+376", ".ad"),
	ANGOLA(Continent.AFRICA, "Luanda","AO", "AGO", "024", "+244", ".ao"),
	ANGUILLA(Continent.NORTH_AMERICA, "The Valley","AI", "AIA", "660", "+1264", ".ai"),
	ANTARCTICA(Continent.ANTARCTICA, "Juneau","AQ", "ATA", null, "+672", ".aq"),
	ANTIGUA_AND_BARBUDA(Continent.NORTH_AMERICA, "Saint John's","AG", "ATG", "028", "+1268", ".ag"),
	ARGENTINA(Continent.SOUTH_AMERICA, "Buenos Aires","AR", "ARG", "032", "+54", ".ar"),
	ARMENIA(Continent.ASIA, "Eriwan","AM", "ARM", "051", "+374", ".am"),
	ARUBA(Continent.NORTH_AMERICA, "Oranjestad","AW", "ABW", null, "+297", ".aw"),
	ASCENSION(Continent.AFRICA, "-/-","AC", "ASC", "826", "+247", ".ac"),
	AUSTRALIA(Continent.AUSTRALIA, "Canberra","AU", "AUS", "036", "+61", ".au"),
	AUSTRIA(Continent.EUROPE, "Wien","AT", "AUT", "040", "+43", ".at"),
	AZERBAIJAN(Continent.ASIA, "Baku","AZ", "AZE", "031", "+994", ".az"),
	BAHAMAS(Continent.NORTH_AMERICA, "Nassau","BS", "BHS", "044", "+1242", ".bs"),
	BAHRAIN(Continent.ASIA, "Manama","BH", "BHR", "048", "+973", ".bh"),
	BANGLADESH(Continent.ASIA, "Dhaka","BD", "BGD", "050", "+880", ".bd"),
	BARBADOS(Continent.NORTH_AMERICA, "Bridgetown","BB", "BRB", "052", "+1246", ".bb"),
	BELARUS(Continent.EUROPE, "Minsk","BY", "BLR", "112", "+375", ".by"),
	BELGIUM(Continent.EUROPE, "Brussels","BE", "BEL", "056", "+32", ".be"),
	BELIZE(Continent.NORTH_AMERICA, "Belmopan","BZ", "BLZ", "084", "+51", ".bz"),
	BENIN(Continent.AFRICA, "Porto Novo","BJ", "BEN", "204", "+229", ".bj"),
	BERMUDA(Continent.NORTH_AMERICA, "Hamilton","BM", "BMU", "060", "+1441", ".bm"),
	BHUTAN(Continent.ASIA, "Thimphu","BT", "BTN", "064", "+975", ".bt"),
	BOLIVIA(Continent.SOUTH_AMERICA, "Sucre","BO", "BOL", "068", "+591", ".bo"),
	BOSNIA_AND_HERZEGOVINA(Continent.EUROPE, "Sarajevo","BA", "BIH", "070", "+387", ".ba"),
	BOTSWANA(Continent.AFRICA, "Gaborone","BW", "BWA", "072", "+267", ".bw"),
	BOUVET_ISLAND(Continent.ANTARCTICA, "(Forschungsinsel)","BV", "BVT", null, "", ".bv"),
	BRAZIL(Continent.SOUTH_AMERICA, "Bras\u00edlia","BR", "BRA", "076", "+55", ".br"),
	BRITISH_INDIAN_OCEAN_TERRITORY(Continent.ASIA, "-/-","IO", "IOT", "086", "", ".io"),
	BRUNEI_DARUSSALAM(Continent.ASIA, "Bandar Seri Begawan","BN", "BRN", "096", "+673", ".bn"),
	BULGARIA(Continent.EUROPE, "Sofia","BG", "BGR", "100", "+359", ".bg"),
	BURKINA_FASO(Continent.AFRICA, "Ouagadougou","BF", "BFA", "854", "+226", ".bf"),
	BURUNDI(Continent.AFRICA, "Bujumbura","BI", "BDI", "108", "+257", ".bi"),
	CAMBODIA(Continent.ASIA, "Phnom Penh","KH", "KHM", "116", "+855", ".kh"),
	CAMEROON(Continent.AFRICA, "Yaound\u00e9","CM", "CMR", "120", "+237", ".cm"),
	CANADA(Continent.NORTH_AMERICA, "Ottawa","CA", "CAN", "124", "+1NXX", ".ca"),
	CANARY_ISLANDS(Continent.EUROPE, "Santa Cruz","IC", "", "724", "", ""),
	CAPE_VERDE(Continent.AFRICA, "Praia","CV", "CPV", "132", "+238", ".cv"),
	CAYMAN_ISLANDS(Continent.NORTH_AMERICA, "George Town","KY", "CYM", "136", "+1345", ".ky"),
	CENTRAL_AFRICAN_REPUBLIC(Continent.AFRICA, "Bangui","CF", "CAF", "140", "+236", ".cf"),
	CHAD(Continent.AFRICA, "N'Djamena","TD", "TCD", "148", "+235", ".td"),
	CHILE(Continent.SOUTH_AMERICA, "Santiago","CL", "CHL", "152", "+56", ".cl"),
	CHINA(Continent.ASIA, "Peking (Beijing)","CN", "CHN", "156", "+86", ".cn"),
	CHRISTMAS_ISLAND(Continent.ASIA, "Flying Fish Cove","CX", "CXR", null, "", ".cx"),
	COCOS_KEELING_ISLANDS(Continent.ASIA, "West Island","CC", "CCK", null, "", ".cc"),
	COLOMBIA(Continent.SOUTH_AMERICA, "Santa F\u00e9 de Bogot\u00e1","CO", "COL", "170", "+57", ".co"),
	COMOROS(Continent.AFRICA, "Moroni","KM", "", "450", "+269", ".km"),
	CONGO(Continent.AFRICA, "Brazzaville","CG", "COG", "178", "+242", ".cg"),
	CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE(Continent.AFRICA, "Kinshasa","CD", "COD", "180", "+243", ".cd"),
	COOK_ISLANDS(Continent.AUSTRALIA, "Avarua","CK", "COK", "184", "+682", ".ck"),
	COSTA_RICA(Continent.NORTH_AMERICA, "San Jos\u00e9","CR", "CRI", "188", "+56", ".cr"),
	CROATIA(Continent.EUROPE, "Zagreb","HR", "HRV", "191", "+385", ".hr"),
	CUBA(Continent.NORTH_AMERICA, "Havanna","CU", "CUB", "192", "+53", ".cu"),
	CYPRUS(Continent.ASIA, "Nikosia","CY", "CYP", "196", "+357", ".cy"),
	CZECH_REPUBLIC(Continent.EUROPE, "Prag","CZ", "CZE", "203", "+420", ".cz"),
	IVORY_COAST(Continent.AFRICA, "Yamoussoukro","CI", "CIV", "384", "+225", ".ci"),
	DENMARK(Continent.EUROPE, "Kopenhagen","DK", "DNK", "208", "+45", ".dk"),
	DIEGO_GARCIA(Continent.AFRICA, "Diego Garcia (Main Island)","DG", "DGA", null, "+246", "-/-"),
	DJIBOUTI(Continent.AFRICA, "Dschibuti","DJ", "DJI", "262", "+253", "dj"),
	DOMINICA(Continent.NORTH_AMERICA, "Roseau","DM", "DMA", "212", "+1767", ".dm"),
	DOMINICAN_REPUBLIC(Continent.SOUTH_AMERICA, "Santo Domingo","DO", "DOM", "214", "+1809", ".do"),
	ENGLAND(Continent.EUROPE, "London","ENG", "ENG", "", "+44", ""),
	ECUADOR(Continent.SOUTH_AMERICA, "Quito","EC", "ECU", "218", "+593", ".ec"),
	EGYPT(Continent.AFRICA, "Kairo","EG", "EGY", "818", "+20", ".eg"),
	EL_SALVADOR(Continent.NORTH_AMERICA, "San Salvador","SV", "SLV", "222", "+53", ".sv"),
	EQUATORIAL_GUINEA(Continent.AFRICA, "Malabo","GQ", "GNQ", "226", "+240", ".gq"),
	ERITREA(Continent.AFRICA, "Asmara (Asmera)","ER", "ERI", "232", "+291", ".er"),
	ESTONIA(Continent.EUROPE, "Tallinn (Reval)","EE", "EST", "233", "+372", ".ee"),
	ETHIOPIA(Continent.AFRICA, "Addis Abeba","ET", "ETH", "231", "+251", ".et"),
	EUROPEAN_UNION(Continent.EUROPE, "Brussels","EU", "-/-", null, "+3883", ".eu"),
	FALKLAND_ISLANDS_MALVINAS(Continent.AFRICA, "Port Stanley","FK", "FLK", "238", "+500", ".fk"),
	FAROE_ISLANDS(Continent.EUROPE, "T\u00f3rshavn","FO", "FRO", null, "+298", ".fo"),
	FIJI(Continent.AUSTRALIA, "Suva","FJ", "FJI", "242", "+679", ".fj"),
	FINLAND(Continent.EUROPE, "Helsinki","FI", "FIN", "246", "+358", ".fi"),
	FRANCE(Continent.EUROPE, "Paris","FR", "FRA", "250", "+33", ".fr"),
	FRENCH_GUIANA(Continent.SOUTH_AMERICA, "Cayenne","GF", "GUF", "254", "+594", ".gf"),
	FRENCH_POLYNESIA(Continent.AUSTRALIA, "Papeete","PF", "PYF", "258", "+689", ".pf"),
	FRENCH_SOUTHERN_TERRITORIES(Continent.ANTARCTICA, "Port-aux-Fran\u00e7ais","TF", "ATF", "250", "", ".tf"),
	GABON(Continent.AFRICA, "Libreville","GA", "GAB", "266", "+241", ".ga"),
	GAMBIA(Continent.AFRICA, "Banjul","GM", "GMB", "270", "+220", ".gm"),
	GEORGIA(Continent.EUROPE, "Tiflis","GE", "GEO", "268", "+995", ".ge"),
	GERMANY(Continent.EUROPE, "Berlin","DE", "DEU", "276", "+49", ".de"),
	GHANA(Continent.AFRICA, "Accra","GH", "GHA", "288", "+233", ".gh"),
	GIBRALTAR(Continent.AFRICA, "Gibraltar","GI", "GIB", "384", "+350", ".gi"),
	GREECE(Continent.EUROPE, "Athen","GR", "GRC", "300", "+30", ".gr"),
	GREENLAND(Continent.NORTH_AMERICA, "Nuuk","GL", "GRL", "304", "+299", ".gl"),
	GRENADA(Continent.NORTH_AMERICA, "St. George's","GD", "GRD", "308", "+1473", ".gd"),
	GUADELOUPE(Continent.NORTH_AMERICA, "Basse-Terre","GP", "GLP", null, "+590", ".gp"),
	GUAM(Continent.ASIA, "Hag\u00e5t\u00f1a","GU", "GUM", null, "+1671", ".gu"),
	GUATEMALA(Continent.NORTH_AMERICA, "Guatemala City","GT", "GTM", "320", "+52", ".gt"),
	GUERNSEY(Continent.EUROPE, "St. Peter Port","GG", "GGY", null, "+44", ".gg"),
	GUINEA(Continent.AFRICA, "Conakry","GN", "GIN", "324", "+224", ".gn"),
	GUINEA_BISSAU(Continent.AFRICA, "Bissau","GW", "GNB", "624", "+245", ".gw"),
	GUYANA(Continent.SOUTH_AMERICA, "Georgetown","GY", "GUY", "328", "+592", ".gy"),
	HAITI(Continent.NORTH_AMERICA, "Port-au-Prince","HT", "HTI", "332", "+59", ".ht"),
	HEARD_ISLAND_AND_MCDONALD_ISLANDS(Continent.AUSTRALIA, "-/-","HM", "HMD", null, "", ".hm"),
	HOLY_SEE_VATICAN_CITY_STATE(Continent.EUROPE, "Vatikan City","VA", "VAT", null, "+3906", ".va"),
	HONDURAS(Continent.NORTH_AMERICA, "Tegucigalpa","HN", "HND", "340", "+54", ".hn"),
	HONG_KONG(Continent.ASIA, "-/-","HK", "HKG", "344", "+852", ".hk"),
	HUNGARY(Continent.EUROPE, "Budapest","HU", "HUN", "348", "+36", ".hu"),
	ICELAND(Continent.EUROPE, "Reykjav\u00edk","IS", "ISL", "352", "+354", ".is"),
	INDIA(Continent.ASIA, "Neu-Delhi","IN", "IND", "356", "+91", ".in"),
	INDONESIA(Continent.ASIA, "Jakarta","ID", "IDN", "360", "+62", ".id"),
	IRAN_ISLAMIC_REPUBLIC_OF(Continent.ASIA, "Teheran","IR", "IRN", "364", "+98", ".ir"),
	IRAQ(Continent.ASIA, "Bagdad","IQ", "IRQ", "368", "+964", ".iq"),
	IRELAND(Continent.EUROPE, "Dublin","IE", "IRL", "372", "+353", ".ie"),
	ISLE_OF_MAN(Continent.EUROPE, "Douglas","IM", "IMN", null, "+44", ".im"),
	ISRAEL(Continent.ASIA, "Jerusalem","IL", "ISR", "376", "+972", ".il"),
	ITALY(Continent.EUROPE, "Rom","IT", "ITA", "380", "+39", ".it"),
	JAMAICA(Continent.NORTH_AMERICA, "Kingston","JM", "JAM", "388", "+1876", ".jm"),
	JAPAN(Continent.ASIA, "Tokio","JP", "JPN", "392", "+81", ".jp"),
	JERSEY(Continent.EUROPE, "Saint Helier","JE", "JEY", null, "+44", ".je"),
	JORDAN(Continent.ASIA, "Amman","JO", "JOR", "400", "+962", ".jo"),
	KAZAKHSTAN(Continent.ASIA, "Astana","KZ", "KAZ", "398", "+7", ".kz"),
	KENYA(Continent.AFRICA, "Nairobi","KE", "KEN", "404", "+254", ".ke"),
	KIRIBATI(Continent.AUSTRALIA, "Bairiki","KI", "KIR", "296", "+686", ".ki"),
	KOREA_DEMOCRATIC_PEOPLE_S_REPUBLIC_OF(Continent.ASIA, "P'y\u014fngyang","KP", "PRK", "408", "+850", ".kp"),
	KOREA_REPUBLIC_OF(Continent.ASIA, "Seoul","KR", "KOR", "410", "+82", ".kr"),
	KUWAIT(Continent.ASIA, "Kuwait","KW", "KWT", "414", "+965", ".kw"),
	KYRGYZSTAN(Continent.ASIA, "Bischkek","KG", "KGZ", null, "+996", ".kg"),
	LAO_PEOPLE_S_DEMOCRATIC_REPUBLIC(Continent.ASIA, "Vientiane","LA", "LAO", "418", "+856", ".la"),
	LATVIA(Continent.EUROPE, "R\u012bga","LV", "LVA", "428", "+371", ".lv"),
	LEBANON(Continent.ASIA, "Beirut","LB", "LBN", "422", "+961", ".lb"),
	LESOTHO(Continent.AFRICA, "Maseru","LS", "LSO", "426", "+266", ".ls"),
	LIBERIA(Continent.AFRICA, "Monrovia","LR", "LBR", "430", "+231", ".lr"),
	LIBYAN_ARAB_JAMAHIRIYA(Continent.AFRICA, "Tripolis","LY", "LBY", "434", "+218", ".ly"),
	LIECHTENSTEIN(Continent.EUROPE, "Vaduz","LI", "LIE", null, "+423", ".li"),
	LITHUANIA(Continent.EUROPE, "Wilna","LT", "LTU", "440", "+370", ".lt"),
	LUXEMBOURG(Continent.EUROPE, "Luxemburg","LU", "LUX", "442", "+352", ".lu"),
	MACAO(Continent.ASIA, "-/-","MO", "MAC", "446", "+853", ".mo"),
	MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF(Continent.EUROPE, "Skopje","MK", "MKD", "807", "+389", ".mk"),
	MADAGASCAR(Continent.AFRICA, "Antananarivo","MG", "MDG", "450", "+261", ".mg"),
	MALAWI(Continent.AFRICA, "Lilongwe","MW", "MWI", "454", "+265", ".mw"),
	MALAYSIA(Continent.ASIA, "Kuala Lumpur","MY", "MYS", "458", "+60", ".my"),
	MALDIVES(Continent.ASIA, "Mal\u00e9","MV", "MDV", "462", "+960", ".mv"),
	MALI(Continent.AFRICA, "Bamako","ML", "MLI", "466", "+223", ".ml"),
	MALTA(Continent.EUROPE, "Valletta","MT", "MLT", "470", "+356", ".mt"),
	MARSHALL_ISLANDS(Continent.AUSTRALIA, "Delap-Uliga-Darrit","MH", "MHL", "584", "+692", ".mh"),
	MARTINIQUE(Continent.NORTH_AMERICA, "Fort-de-France","MQ", "MTQ", null, "+596", ".mq"),
	MAURITANIA(Continent.AFRICA, "Nouakchott","MR", "MRT", "478", "+222", ".mr"),
	MAURITIUS(Continent.AFRICA, "Port Louis","MU", "MUS", "480", "+230", ".mu"),
	MAYOTTE(Continent.AFRICA, "Mamoudzou","YT", "MYT", null, "+269", ".yt"),
	MEXICO(Continent.NORTH_AMERICA, "Mexico City","MX", "MEX", "484", "+52", ".mx"),
	MICRONESIA_FEDERATED_STATES_OF(Continent.AUSTRALIA, "Palikir","FM", "FSM", "583", "+691", ".fm"),
	MOLDOVA(Continent.EUROPE, "Chi\u015fin\u0103u","MD", "MDA", "498", "+373", ".md"),
	MONACO(Continent.EUROPE, "Monaco","MC", "MCO", null, "+377", ".mc"),
	MONGOLIA(Continent.ASIA, "Ulaanbaatar","MN", "MNG", "496", "+976", ".mn"),
	MONTENEGRO(Continent.EUROPE, "Podgorica","ME", "MNE", "090", "+382", ".me"),
	MONTSERRAT(Continent.NORTH_AMERICA, "Plymouth","MS", "MSR", "500", "+1664", ".ms"),
	MOROCCO(Continent.AFRICA, "Rabat","MA", "MAR", "504", "+211", ".ma"),
	MOZAMBIQUE(Continent.AFRICA, "Maputo","MZ", "MOZ", "508", "+258", ".mz"),
	MYANMAR(Continent.ASIA, "Rangun","MM", "MMR", "104", "+95", ".mm"),
	NAMIBIA(Continent.AFRICA, "Windhoek","NA", "NAM", "516", "+264", ".na"),
	NAURU(Continent.AUSTRALIA, "Yaren","NR", "NRU", "520", "+674", ".nr"),
	NEPAL(Continent.ASIA, "Kathmandu","NP", "NPL", "524", "+977", ".np"),
	NETHERLANDS(Continent.EUROPE, "Amsterdam","NL", "NLD", "528", "+31", ".nl"),
	NETHERLANDS_ANTILLES(Continent.NORTH_AMERICA, "Willemstad","AN", "ANT", "530", "+599", ".an"),
	NEW_CALEDONIA(Continent.AUSTRALIA, "Noum\u00e9a","NC", "NCL", "540", "+687", ".nc"),
	NEW_ZEALAND(Continent.AUSTRALIA, "Wellington","NZ", "NZL", "554", "+64", ".nz"),
	NICARAGUA(Continent.NORTH_AMERICA, "Managua","NI", "NIC", "558", "+55", ".ni"),
	NIGER(Continent.AFRICA, "Niamey","NE", "NER", "562", "+227", ".ne"),
	NIGERIA(Continent.AFRICA, "Abuja","NG", "NGA", "566", "+234", ".ng"),
	NIUE(Continent.AUSTRALIA, "Alofi","NU", "NIU", "570", "+683", ".nu"),
	NORFOLK_ISLAND(Continent.AUSTRALIA, "Kingston","NF", "NFK", null, "+6723", ".nf"),
	NORTHERN_MARIANA_ISLANDS(Continent.AUSTRALIA, "Saipan","MP", "MNP", "840", "+1670", ".mp"),
	NORWAY(Continent.EUROPE, "Oslo","NO", "NOR", "578", "+47", ".no"),
	OMAN(Continent.ASIA, "Maskat","OM", "OMN", "512", "+968", ".om"),
	PAKISTAN(Continent.ASIA, "Islamabad","PK", "PAK", "586", "+92", ".pk"),
	PALAU(Continent.AUSTRALIA, "Melekeok","PW", "PLW", null, "+680", ".pw"),
	PALESTINIAN_TERRITORY_OCCUPIED(Continent.ASIA, "Ramallah","PS", "PSE", null, "+970", ".ps"),
	PANAMA(Continent.SOUTH_AMERICA, "Panama City","PA", "PAN", "591", "+57", ".pa"),
	PAPUA_NEW_GUINEA(Continent.AUSTRALIA, "Port Moresby","PG", "PNG", "598", "+675", ".pg"),
	PARAGUAY(Continent.SOUTH_AMERICA, "Asunci\u00f3n","PY", "PRY", "600", "+595", ".py"),
	PERU(Continent.SOUTH_AMERICA, "Lima","PE", "PER", "604", "+51", ".pe"),
	PHILIPPINES(Continent.ASIA, "Manila","PH", "PHL", "608", "+63", ".ph"),
	PITCAIRN(Continent.AUSTRALIA, "Adamstown","PN", "PCN", null, "+649", ".pn"),
	POLAND(Continent.EUROPE, "Warszaw","PL", "POL", "616", "+48", ".pl"),
	PORTUGAL(Continent.EUROPE, "Lissabon","PT", "PRT", "620", "+351", ".pt"),
	PUERTO_RICO(Continent.NORTH_AMERICA, "San Juan","PR", "PRI", "630", "+1939", ".pr"),
	QATAR(Continent.ASIA, "Doha","QA", "QAT", "634", "+974", ".qa"),
	ROMANIA(Continent.EUROPE, "Bucarest","RO", "ROU", "642", "+40", ".ro"),
	RUSSIAN_FEDERATION(Continent.ASIA, "Moskau","RU", "RUS", "643", "+7", ".ru"),
	RWANDA(Continent.AFRICA, "Kigali","RW", "RWA", "646", "+250", ".rw"),
	RÉUNION(Continent.AFRICA, "Saint-Denis","RE", "REU", null, "+262", ".re"),
	SAINT_HELENA(Continent.AFRICA, "Jamestown","SH", "SHN", null, "+290", ".sh"),
	SAINT_KITTS_AND_NEVIS(Continent.NORTH_AMERICA, "Basseterre","KN", "KNA", "659", "+1869", ".kn"),
	SAINT_LUCIA(Continent.SOUTH_AMERICA, "Castries","LC", "LCA", "662", "+1758", ".lc"),
	SAINT_PIERRE_AND_MIQUELON(Continent.NORTH_AMERICA, "Saint-Pierre","PM", "SPM", null, "+508", ".pm"),
	SAINT_VINCENT_AND_THE_GRENADINES(Continent.SOUTH_AMERICA, "Kingstown","VC", "VCT", "670", "+1784", ".vc"),
	SAMOA(Continent.AUSTRALIA, "Apia","WS", "WSM", "882", "", ".ws"),
	SAN_MARINO(Continent.EUROPE, "San Marino","SM", "SMR", null, "+378", ".sm"),
	SAO_TOME_AND_PRINCIPE(Continent.AFRICA, "S\u00e3o Tom\u00e9","ST", "STP", "678", "+239", ".st"),
	SAUDI_ARABIA(Continent.ASIA, "Riad","SA", "SAU", "682", "+966", ".sa"),
	SAUDI_IRAQI_NEUTRAL_ZONE(Continent.ASIA, "-/-","NT", "NTZ", null, "", ".nt"),
	SCOTLAND(Continent.EUROPE, "Edinburgh","SCO", "SCO", "", "+44", ""),
	SENEGAL(Continent.AFRICA, "Dakar","SN", "SEN", "686", "+221", ".sn"),
	SERBIA(Continent.EUROPE, "Belgrad","RS", "SRB", "381", "+381", ".rs"),
	SERBIEN_UND_MONTENEGRO(Continent.EUROPE, "Belgrad","CS", "SCG", "090", "+381", ".cs"),
	SEYCHELLES(Continent.AFRICA, "Victoria","SC", "SYC", "690", "+248", ".sc"),
	SIERRA_LEONE(Continent.AFRICA, "Freetown","SL", "SLE", "694", "+232", ".sl"),
	SINGAPORE(Continent.ASIA, "Singapur","SG", "SGP", "702", "+65", ".sg"),
	SLOVAKIA(Continent.EUROPE, "Bratislava","SK", "SVK", "703", "+421", ".sk"),
	SLOVENIA(Continent.EUROPE, "Ljubljana","SI", "SVN", "705", "+386", ".si"),
	SOLOMON_ISLANDS(Continent.AUSTRALIA, "Honiara","SB", "SLB", "090", "+677", ".sb"),
	SOMALIA(Continent.AFRICA, "Mogadischu","SO", "SOM", "706", "+252", ".so"),
	SOUTH_AFRICA(Continent.AFRICA, "Tshwane / Pretoria","ZA", "ZAF", "710", "+27", ".za"),
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS(Continent.SOUTH_AMERICA, "Grytviken","GS", "SGS", null, "", ""),
	SOVIET_UNION(Continent.EUROPE, "Moskau","SU", "SUN", null, "", ".su"),
	SPAIN(Continent.EUROPE, "Madrid","ES", "ESP", "724", "+34", ".es"),
	SRI_LANKA(Continent.ASIA, "Colombo","LK", "LKA", "144", "+94", ".lk"),
	SUDAN(Continent.AFRICA, "Khartum","SD", "SDN", "736", "+249", ".sd"),
	SURINAME(Continent.SOUTH_AMERICA, "Paramaribo","SR", "SUR", "740", "+597", ".sr"),
	SVALBARD_AND_JAN_MAYEN(Continent.EUROPE, "Longyearbyen","SJ", "SJM", null, "", ".sj"),
	SWAZILAND(Continent.AFRICA, "Mbabane","SZ", "SWZ", "748", "+268", ".sz"),
	SWEDEN(Continent.EUROPE, "Stockholm","SE", "SWE", "752", "+46", ".se"),
	SWITZERLAND(Continent.EUROPE, "Bern","CH", "CHE", "756", "+41", ".ch"),
	SYRIAN_ARAB_REPUBLIC(Continent.ASIA, "Damaskus","SY", "SYR", "760", "+963", ".sy"),
	TAIWAN(Continent.ASIA, "Taipeh","TW", "TWN", "158", "+886", ".tw"),
	TAJIKISTAN(Continent.ASIA, "Duschanbe","TJ", "TJK", null, "+992", ".tj"),
	TANZANIA_UNITED_REPUBLIC_OF(Continent.AFRICA, "Dodoma","TZ", "TZA", "834", "+255", ".tz"),
	THAILAND(Continent.ASIA, "Bangkok","TH", "THA", "764", "+66", ".th"),
	TIMOR_LESTE(Continent.AUSTRALIA, "Dili","TL", "TLS", null, "+670", ".tl"),
	TOGO(Continent.AFRICA, "Lom\u00e9","TG", "TGO", "768", "+228", ".tg"),
	TOKELAU(Continent.AUSTRALIA, "-/-","TK", "TKL", null, "+690", ".tk"),
	TONGA(Continent.AUSTRALIA, "Nuku\u2019alofa","TO", "TON", null, "+676", ".to"),
	TRINIDAD_AND_TOBAGO(Continent.SOUTH_AMERICA, "Port-of-Spain","TT", "TTO", "780", "+1868", ".tt"),
	TRISTAN_DA_CUNHA(Continent.AFRICA, "Jamestown","TA", "TAA", null, "+290", ""),
	TUNISIA(Continent.AFRICA, "Tunis","TN", "TUN", "788", "+216", ".tn"),
	TURKEY(Continent.ASIA, "Ankara","TR", "TUR", "792", "+90", ".tr"),
	TURKMENISTAN(Continent.ASIA, "A\u015fgabat","TM", "TKM", null, "+993", ".tm"),
	TURKS_AND_CAICOS_ISLANDS(Continent.NORTH_AMERICA, "Cockburn Town auf Grand Turk","TC", "TCA", "796", "+1649", ".tc"),
	TUVALU(Continent.AUSTRALIA, "Funafuti","TV", "TUV", null, "+688", ".tv"),
	UGANDA(Continent.AFRICA, "Kampala","UG", "UGA", "800", "+256", ".ug"),
	UKRAINE(Continent.EUROPE, "Kiev","UA", "UKR", "804", "+380", ".ua"),
	UNITED_ARAB_EMIRATES(Continent.ASIA, "Abu Dhabi","AE", "ARE", "784", "+971", ".ae"),
	UNITED_KINGDOM(Continent.EUROPE, "London","GB", "GBR", "826", "+44", ".gb"),
	UNITED_STATES(Continent.NORTH_AMERICA, "Washington, D.C.","US", "USA", "840", "+1", ".us"),
	URUGUAY(Continent.SOUTH_AMERICA, "Montevideo","UY", "URY", "858", "+598", ".uy"),
	UZBEKISTAN(Continent.ASIA, "Taschkent","UZ", "UZB", "860", "+998", ".uz"),
	VANUATU(Continent.AUSTRALIA, "Port Vila","VU", "VUT", "548", "+678", ".vu"),
	VENEZUELA(Continent.SOUTH_AMERICA, "Caracas","VE", "VEN", "862", "+58", ".ve"),
	VIET_NAM(Continent.ASIA, "H\u00e0 N\u1ed9i","VN", "VNM", "704", "+84", ".vn"),
	VIRGIN_ISLANDS_BRITISH(Continent.NORTH_AMERICA, "Road Town","VG", "VGB", "092", "+1284", ".vg"),
	VIRGIN_ISLANDS_US(Continent.SOUTH_AMERICA, "Charlotte Amalie","VI", "VIR", "850", "+1340", ".vi"),
	WALES(Continent.EUROPE, "Cardiff","WAL", "WAL", "", "+44", ""),
	WALLIS_AND_FUTUNA(Continent.AUSTRALIA, "Mata-Utu","WF", "WLF", "876", "+681", ".wf"),
	WESTERN_SAHARA(Continent.AFRICA, "El Aai\u00fan","EH", "ESH", null, "", ".eh"),
	YEMEN(Continent.ASIA, "Sanaa","YE", "YEM", "887", "+967", ".ye"),
	ZAMBIA(Continent.AFRICA, "Lusaka","ZM", "ZMB", "894", "+260", ".zm"),
	ZIMBABWE(Continent.AFRICA, "Harare","ZW", "ZWE", "716", "+263", ".zw"),
	ÅLAND_ISLANDS(Continent.EUROPE, "Mariehamn","AX", "ALA", null, "+35818", ".ax");
	
	/** continent this country is situated in */
	private Continent continent;
	/** capital of this country */
	private String capital;
	/** ISO 2 country code */
	private String iso2Code;
	/** ISO 3 country code */
	private String iso3Code;
	/** ISO 3 country digit code */
	private String iso3DigitCode;
	/** International dial code */
	private String idc;
	/** Top Level Domain */
	private String tld;
	
	/**
	 * Constructor.
	 * @param continent continent this country is situated in
	 * @param capital capital of this country
	 * @param iso2Code ISO 2 country code
	 * @param iso3Code ISO 3 country code
	 * @param idc International dial code
	 * @param tld Top Level Domain
	 */
	private Country(Continent continent, String capital, String iso2Code, String iso3Code, String iso3DigitCode, String idc, String tld) {
		this.continent = continent;
		this.capital = capital;
		this.iso2Code = iso2Code;
		this.iso3Code = iso3Code;
		this.iso3DigitCode = iso3DigitCode;
		this.idc = idc;
		this.tld = tld;
	}
	
	/**
	 * Returns the continent this country is situated in.
	 * @return the continent
	 */
	public Continent getContinent() {
		return continent;
	}
	
	/**
	 * Returns the capital of this country.
	 * @return the capital
	 */
	public String getCapital() {
		return capital;
	}
	
	/**
	 * Returns the ISO 2 country code.
	 * @return the iso2Code
	 */
	public String getIso2Code() {
		return iso2Code;
	}
	
	/**
	 * Returns the ISO 3 country code.
	 * @return the iso3Code
	 */
	public String getIso3Code() {
		return iso3Code;
	}
	
	/**
	 * Returns the iso3DigitCode.
	 * @return the iso3DigitCode
	 */
	public String getIso3DigitCode() {
		return iso3DigitCode;
	}

	/**
	 * Returns the international dial code.
	 * @return the idc
	 */
	public String getIdc() {
		return idc;
	}
	
	/**
	 * Returns the Top Level Domain.
	 * @return the tld
	 */
	public String getTld() {
		return tld;
	}
	
	
}
