/**
 * 
 */
package rs.baselib.type;

/**
 * Countries on this planet.
 * @author ralph
 *
 */
public enum Country {

	AFGHANISTAN(Continent.ASIA, "Kabul","AF", "AFG", "+93", ".af"),
	ALBANIA(Continent.EUROPE, "Tirana","AL", "ALB", "+355", ".al"),
	ALGERIA(Continent.AFRICA, "Algier","DZ", "DZA", "+213", ".dz"),
	AMERICAN_SAMOA(Continent.AUSTRALIA, "Pago-Pago","AS", "ASM", "+1684", ".as"),
	ANDORRA(Continent.EUROPE, "Andorra la Vella","AD", "AND", "+376", ".ad"),
	ANGOLA(Continent.AFRICA, "Luanda","AO", "AGO", "+244", ".ao"),
	ANGUILLA(Continent.NORTH_AMERICA, "The Valley","AI", "AIA", "+1264", ".ai"),
	ANTARCTICA(Continent.ANTARCTICA, "Juneau","AQ", "ATA", "+672", ".aq"),
	ANTIGUA_AND_BARBUDA(Continent.NORTH_AMERICA, "Saint John's","AG", "ATG", "+1268", ".ag"),
	ARGENTINA(Continent.SOUTH_AMERICA, "Buenos Aires","AR", "ARG", "+54", ".ar"),
	ARMENIA(Continent.ASIA, "Eriwan","AM", "ARM", "+374", ".am"),
	ARUBA(Continent.NORTH_AMERICA, "Oranjestad","AW", "ABW", "+297", ".aw"),
	ASCENSION(Continent.AFRICA, "-/-","AC", "ASC", "+247", ".ac"),
	AUSTRALIA(Continent.AUSTRALIA, "Canberra","AU", "AUS", "+61", ".au"),
	AUSTRIA(Continent.EUROPE, "Wien","AT", "AUT", "+43", ".at"),
	AZERBAIJAN(Continent.ASIA, "Baku","AZ", "AZE", "+994", ".az"),
	BAHAMAS(Continent.NORTH_AMERICA, "Nassau","BS", "BHS", "+1242", ".bs"),
	BAHRAIN(Continent.ASIA, "Manama","BH", "BHR", "+973", ".bh"),
	BANGLADESH(Continent.ASIA, "Dhaka","BD", "BGD", "+880", ".bd"),
	BARBADOS(Continent.NORTH_AMERICA, "Bridgetown","BB", "BRB", "+1246", ".bb"),
	BELARUS(Continent.EUROPE, "Minsk","BY", "BLR", "+375", ".by"),
	BELGIUM(Continent.EUROPE, "Brussels","BE", "BEL", "+32", ".be"),
	BELIZE(Continent.NORTH_AMERICA, "Belmopan","BZ", "BLZ", "+51", ".bz"),
	BENIN(Continent.AFRICA, "Porto Novo","BJ", "BEN", "+229", ".bj"),
	BERMUDA(Continent.NORTH_AMERICA, "Hamilton","BM", "BMU", "+1441", ".bm"),
	BHUTAN(Continent.ASIA, "Thimphu","BT", "BTN", "+975", ".bt"),
	BOLIVIA(Continent.SOUTH_AMERICA, "Sucre","BO", "BOL", "+591", ".bo"),
	BOSNIA_AND_HERZEGOVINA(Continent.EUROPE, "Sarajevo","BA", "BIH", "+387", ".ba"),
	BOTSWANA(Continent.AFRICA, "Gaborone","BW", "BWA", "+267", ".bw"),
	BOUVET_ISLAND(Continent.ANTARCTICA, "(Forschungsinsel)","BV", "BVT", "", ".bv"),
	BRAZIL(Continent.SOUTH_AMERICA, "Bras\u00edlia","BR", "BRA", "+55", ".br"),
	BRITISH_INDIAN_OCEAN_TERRITORY(Continent.ASIA, "-/-","IO", "IOT", "", ".io"),
	BRUNEI_DARUSSALAM(Continent.ASIA, "Bandar Seri Begawan","BN", "BRN", "+673", ".bn"),
	BULGARIA(Continent.EUROPE, "Sofia","BG", "BGR", "+359", ".bg"),
	BURKINA_FASO(Continent.AFRICA, "Ouagadougou","BF", "BFA", "+226", ".bf"),
	BURUNDI(Continent.AFRICA, "Bujumbura","BI", "BDI", "+257", ".bi"),
	CAMBODIA(Continent.ASIA, "Phnom Penh","KH", "KHM", "+855", ".kh"),
	CAMEROON(Continent.AFRICA, "Yaound\u00e9","CM", "CMR", "+237", ".cm"),
	CANADA(Continent.NORTH_AMERICA, "Ottawa","CA", "CAN", "+1NXX", ".ca"),
	CANARY_ISLANDS(Continent.EUROPE, "Santa Cruz","IC", "", "", ""),
	CAPE_VERDE(Continent.AFRICA, "Praia","CV", "CPV", "+238", ".cv"),
	CAYMAN_ISLANDS(Continent.NORTH_AMERICA, "George Town","KY", "CYM", "+1345", ".ky"),
	CENTRAL_AFRICAN_REPUBLIC(Continent.AFRICA, "Bangui","CF", "CAF", "+236", ".cf"),
	CHAD(Continent.AFRICA, "N'Djamena","TD", "TCD", "+235", ".td"),
	CHILE(Continent.SOUTH_AMERICA, "Santiago","CL", "CHL", "+56", ".cl"),
	CHINA(Continent.ASIA, "Peking (Beijing)","CN", "CHN", "+86", ".cn"),
	CHRISTMAS_ISLAND(Continent.ASIA, "Flying Fish Cove","CX", "CXR", "", ".cx"),
	COCOS_KEELING_ISLANDS(Continent.ASIA, "West Island","CC", "CCK", "", ".cc"),
	COLOMBIA(Continent.SOUTH_AMERICA, "Santa F\u00e9 de Bogot\u00e1","CO", "COL", "+57", ".co"),
	COMOROS(Continent.AFRICA, "Moroni","KM", "", "+269", ".km"),
	CONGO(Continent.AFRICA, "Brazzaville","CG", "COG", "+242", ".cg"),
	CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE(Continent.AFRICA, "Kinshasa","CD", "COD", "+243", ".cd"),
	COOK_ISLANDS(Continent.AUSTRALIA, "Avarua","CK", "COK", "+682", ".ck"),
	COSTA_RICA(Continent.NORTH_AMERICA, "San Jos\u00e9","CR", "CRI", "+56", ".cr"),
	CROATIA(Continent.EUROPE, "Zagreb","HR", "HRV", "+385", ".hr"),
	CUBA(Continent.NORTH_AMERICA, "Havanna","CU", "CUB", "+53", ".cu"),
	CYPRUS(Continent.ASIA, "Nikosia","CY", "CYP", "+357", ".cy"),
	CZECH_REPUBLIC(Continent.EUROPE, "Prag","CZ", "CZE", "+420", ".cz"),
	CÔTE_D_IVOIRE(Continent.AFRICA, "Yamoussoukro","CI", "CIV", "+225", ".ci"),
	DENMARK(Continent.EUROPE, "Kopenhagen","DK", "DNK", "+45", ".dk"),
	DIEGO_GARCIA(Continent.AFRICA, "Diego Garcia (Main Island)","DG", "DGA", "+246", "-/-"),
	DJIBOUTI(Continent.AFRICA, "Dschibuti","DJ", "DJI", "+253", "dj"),
	DOMINICA(Continent.NORTH_AMERICA, "Roseau","DM", "DMA", "+1767", ".dm"),
	DOMINICAN_REPUBLIC(Continent.SOUTH_AMERICA, "Santo Domingo","DO", "DOM", "+1809", ".do"),
	ECUADOR(Continent.SOUTH_AMERICA, "Quito","EC", "ECU", "+593", ".ec"),
	EGYPT(Continent.AFRICA, "Kairo","EG", "EGY", "+20", ".eg"),
	EL_SALVADOR(Continent.NORTH_AMERICA, "San Salvador","SV", "SLV", "+53", ".sv"),
	EQUATORIAL_GUINEA(Continent.AFRICA, "Malabo","GQ", "GNQ", "+240", ".gq"),
	ERITREA(Continent.AFRICA, "Asmara (Asmera)","ER", "ERI", "+291", ".er"),
	ESTONIA(Continent.EUROPE, "Tallinn (Reval)","EE", "EST", "+372", ".ee"),
	ETHIOPIA(Continent.AFRICA, "Addis Abeba","ET", "ETH", "+251", ".et"),
	EUROPEAN_UNION(Continent.EUROPE, "Brussels","EU", "-/-", "+3883", ".eu"),
	FALKLAND_ISLANDS_MALVINAS(Continent.AFRICA, "Port Stanley","FK", "FLK", "+500", ".fk"),
	FAROE_ISLANDS(Continent.EUROPE, "T\u00f3rshavn","FO", "FRO", "+298", ".fo"),
	FIJI(Continent.AUSTRALIA, "Suva","FJ", "FJI", "+679", ".fj"),
	FINLAND(Continent.EUROPE, "Helsinki","FI", "FIN", "+358", ".fi"),
	FRANCE(Continent.EUROPE, "Paris","FR", "FRA", "+33", ".fr"),
	FRENCH_GUIANA(Continent.SOUTH_AMERICA, "Cayenne","GF", "GUF", "+594", ".gf"),
	FRENCH_POLYNESIA(Continent.AUSTRALIA, "Papeete","PF", "PYF", "+689", ".pf"),
	FRENCH_SOUTHERN_TERRITORIES(Continent.ANTARCTICA, "Port-aux-Fran\u00e7ais","TF", "ATF", "", ".tf"),
	GABON(Continent.AFRICA, "Libreville","GA", "GAB", "+241", ".ga"),
	GAMBIA(Continent.AFRICA, "Banjul","GM", "GMB", "+220", ".gm"),
	GEORGIA(Continent.EUROPE, "Tiflis","GE", "GEO", "+995", ".ge"),
	GERMANY(Continent.EUROPE, "Berlin","DE", "DEU", "+49", ".de"),
	GHANA(Continent.AFRICA, "Accra","GH", "GHA", "+233", ".gh"),
	GIBRALTAR(Continent.AFRICA, "Gibraltar","GI", "GIB", "+350", ".gi"),
	GREECE(Continent.EUROPE, "Athen","GR", "GRC", "+30", ".gr"),
	GREENLAND(Continent.NORTH_AMERICA, "Nuuk","GL", "GRL", "+299", ".gl"),
	GRENADA(Continent.NORTH_AMERICA, "St. George's","GD", "GRD", "+1473", ".gd"),
	GUADELOUPE(Continent.NORTH_AMERICA, "Basse-Terre","GP", "GLP", "+590", ".gp"),
	GUAM(Continent.ASIA, "Hag\u00e5t\u00f1a","GU", "GUM", "+1671", ".gu"),
	GUATEMALA(Continent.NORTH_AMERICA, "Guatemala City","GT", "GTM", "+52", ".gt"),
	GUERNSEY(Continent.EUROPE, "St. Peter Port","GG", "GGY", "+44", ".gg"),
	GUINEA(Continent.AFRICA, "Conakry","GN", "GIN", "+224", ".gn"),
	GUINEA_BISSAU(Continent.AFRICA, "Bissau","GW", "GNB", "+245", ".gw"),
	GUYANA(Continent.SOUTH_AMERICA, "Georgetown","GY", "GUY", "+592", ".gy"),
	HAITI(Continent.NORTH_AMERICA, "Port-au-Prince","HT", "HTI", "+59", ".ht"),
	HEARD_ISLAND_AND_MCDONALD_ISLANDS(Continent.AUSTRALIA, "-/-","HM", "HMD", "", ".hm"),
	HOLY_SEE_VATICAN_CITY_STATE(Continent.EUROPE, "Vatikan City","VA", "VAT", "+3906", ".va"),
	HONDURAS(Continent.NORTH_AMERICA, "Tegucigalpa","HN", "HND", "+54", ".hn"),
	HONG_KONG(Continent.ASIA, "-/-","HK", "HKG", "+852", ".hk"),
	HUNGARY(Continent.EUROPE, "Budapest","HU", "HUN", "+36", ".hu"),
	ICELAND(Continent.EUROPE, "Reykjav\u00edk","IS", "ISL", "+354", ".is"),
	INDIA(Continent.ASIA, "Neu-Delhi","IN", "IND", "+91", ".in"),
	INDONESIA(Continent.ASIA, "Jakarta","ID", "IDN", "+62", ".id"),
	IRAN_ISLAMIC_REPUBLIC_OF(Continent.ASIA, "Teheran","IR", "IRN", "+98", ".ir"),
	IRAQ(Continent.ASIA, "Bagdad","IQ", "IRQ", "+964", ".iq"),
	IRELAND(Continent.EUROPE, "Dublin","IE", "IRL", "+353", ".ie"),
	ISLE_OF_MAN(Continent.EUROPE, "Douglas","IM", "IMN", "+44", ".im"),
	ISRAEL(Continent.ASIA, "Jerusalem","IL", "ISR", "+972", ".il"),
	ITALY(Continent.EUROPE, "Rom","IT", "ITA", "+39", ".it"),
	JAMAICA(Continent.NORTH_AMERICA, "Kingston","JM", "JAM", "+1876", ".jm"),
	JAPAN(Continent.ASIA, "Tokio","JP", "JPN", "+81", ".jp"),
	JERSEY(Continent.EUROPE, "Saint Helier","JE", "JEY", "+44", ".je"),
	JORDAN(Continent.ASIA, "Amman","JO", "JOR", "+962", ".jo"),
	KAZAKHSTAN(Continent.ASIA, "Astana","KZ", "KAZ", "+7", ".kz"),
	KENYA(Continent.AFRICA, "Nairobi","KE", "KEN", "+254", ".ke"),
	KIRIBATI(Continent.AUSTRALIA, "Bairiki","KI", "KIR", "+686", ".ki"),
	KOREA_DEMOCRATIC_PEOPLE_S_REPUBLIC_OF(Continent.ASIA, "P'y\u014fngyang","KP", "PRK", "+850", ".kp"),
	KOREA_REPUBLIC_OF(Continent.ASIA, "Seoul","KR", "KOR", "+82", ".kr"),
	KUWAIT(Continent.ASIA, "Kuwait","KW", "KWT", "+965", ".kw"),
	KYRGYZSTAN(Continent.ASIA, "Bischkek","KG", "KGZ", "+996", ".kg"),
	LAO_PEOPLE_S_DEMOCRATIC_REPUBLIC(Continent.ASIA, "Vientiane","LA", "LAO", "+856", ".la"),
	LATVIA(Continent.EUROPE, "R\u012bga","LV", "LVA", "+371", ".lv"),
	LEBANON(Continent.ASIA, "Beirut","LB", "LBN", "+961", ".lb"),
	LESOTHO(Continent.AFRICA, "Maseru","LS", "LSO", "+266", ".ls"),
	LIBERIA(Continent.AFRICA, "Monrovia","LR", "LBR", "+231", ".lr"),
	LIBYAN_ARAB_JAMAHIRIYA(Continent.AFRICA, "Tripolis","LY", "LBY", "+218", ".ly"),
	LIECHTENSTEIN(Continent.EUROPE, "Vaduz","LI", "LIE", "+423", ".li"),
	LITHUANIA(Continent.EUROPE, "Wilna","LT", "LTU", "+370", ".lt"),
	LUXEMBOURG(Continent.EUROPE, "Luxemburg","LU", "LUX", "+352", ".lu"),
	MACAO(Continent.ASIA, "-/-","MO", "MAC", "+853", ".mo"),
	MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF(Continent.EUROPE, "Skopje","MK", "MKD", "+389", ".mk"),
	MADAGASCAR(Continent.AFRICA, "Antananarivo","MG", "MDG", "+261", ".mg"),
	MALAWI(Continent.AFRICA, "Lilongwe","MW", "MWI", "+265", ".mw"),
	MALAYSIA(Continent.ASIA, "Kuala Lumpur","MY", "MYS", "+60", ".my"),
	MALDIVES(Continent.ASIA, "Mal\u00e9","MV", "MDV", "+960", ".mv"),
	MALI(Continent.AFRICA, "Bamako","ML", "MLI", "+223", ".ml"),
	MALTA(Continent.EUROPE, "Valletta","MT", "MLT", "+356", ".mt"),
	MARSHALL_ISLANDS(Continent.AUSTRALIA, "Delap-Uliga-Darrit","MH", "MHL", "+692", ".mh"),
	MARTINIQUE(Continent.NORTH_AMERICA, "Fort-de-France","MQ", "MTQ", "+596", ".mq"),
	MAURITANIA(Continent.AFRICA, "Nouakchott","MR", "MRT", "+222", ".mr"),
	MAURITIUS(Continent.AFRICA, "Port Louis","MU", "MUS", "+230", ".mu"),
	MAYOTTE(Continent.AFRICA, "Mamoudzou","YT", "MYT", "+269", ".yt"),
	MEXICO(Continent.NORTH_AMERICA, "Mexico City","MX", "MEX", "+52", ".mx"),
	MICRONESIA_FEDERATED_STATES_OF(Continent.AUSTRALIA, "Palikir","FM", "FSM", "+691", ".fm"),
	MOLDOVA(Continent.EUROPE, "Chi\u015fin\u0103u","MD", "MDA", "+373", ".md"),
	MONACO(Continent.EUROPE, "Monaco","MC", "MCO", "+377", ".mc"),
	MONGOLIA(Continent.ASIA, "Ulaanbaatar","MN", "MNG", "+976", ".mn"),
	MONTENEGRO(Continent.EUROPE, "Podgorica","ME", "MNE", "+382", ".me"),
	MONTSERRAT(Continent.NORTH_AMERICA, "Plymouth","MS", "MSR", "+1664", ".ms"),
	MOROCCO(Continent.AFRICA, "Rabat","MA", "MAR", "+211", ".ma"),
	MOZAMBIQUE(Continent.AFRICA, "Maputo","MZ", "MOZ", "+258", ".mz"),
	MYANMAR(Continent.ASIA, "Rangun","MM", "MMR", "+95", ".mm"),
	NAMIBIA(Continent.AFRICA, "Windhoek","NA", "NAM", "+264", ".na"),
	NAURU(Continent.AUSTRALIA, "Yaren","NR", "NRU", "+674", ".nr"),
	NEPAL(Continent.ASIA, "Kathmandu","NP", "NPL", "+977", ".np"),
	NETHERLANDS(Continent.EUROPE, "Amsterdam","NL", "NLD", "+31", ".nl"),
	NETHERLANDS_ANTILLES(Continent.NORTH_AMERICA, "Willemstad","AN", "ANT", "+599", ".an"),
	NEW_CALEDONIA(Continent.AUSTRALIA, "Noum\u00e9a","NC", "NCL", "+687", ".nc"),
	NEW_ZEALAND(Continent.AUSTRALIA, "Wellington","NZ", "NZL", "+64", ".nz"),
	NICARAGUA(Continent.NORTH_AMERICA, "Managua","NI", "NIC", "+55", ".ni"),
	NIGER(Continent.AFRICA, "Niamey","NE", "NER", "+227", ".ne"),
	NIGERIA(Continent.AFRICA, "Abuja","NG", "NGA", "+234", ".ng"),
	NIUE(Continent.AUSTRALIA, "Alofi","NU", "NIU", "+683", ".nu"),
	NORFOLK_ISLAND(Continent.AUSTRALIA, "Kingston","NF", "NFK", "+6723", ".nf"),
	NORTHERN_MARIANA_ISLANDS(Continent.AUSTRALIA, "Saipan","MP", "MNP", "+1670", ".mp"),
	NORWAY(Continent.EUROPE, "Oslo","NO", "NOR", "+47", ".no"),
	OMAN(Continent.ASIA, "Maskat","OM", "OMN", "+968", ".om"),
	PAKISTAN(Continent.ASIA, "Islamabad","PK", "PAK", "+92", ".pk"),
	PALAU(Continent.AUSTRALIA, "Melekeok","PW", "PLW", "+680", ".pw"),
	PALESTINIAN_TERRITORY_OCCUPIED(Continent.ASIA, "Ramallah","PS", "PSE", "+970", ".ps"),
	PANAMA(Continent.SOUTH_AMERICA, "Panama City","PA", "PAN", "+57", ".pa"),
	PAPUA_NEW_GUINEA(Continent.AUSTRALIA, "Port Moresby","PG", "PNG", "+675", ".pg"),
	PARAGUAY(Continent.SOUTH_AMERICA, "Asunci\u00f3n","PY", "PRY", "+595", ".py"),
	PERU(Continent.SOUTH_AMERICA, "Lima","PE", "PER", "+51", ".pe"),
	PHILIPPINES(Continent.ASIA, "Manila","PH", "PHL", "+63", ".ph"),
	PITCAIRN(Continent.AUSTRALIA, "Adamstown","PN", "PCN", "+649", ".pn"),
	POLAND(Continent.EUROPE, "Warszaw","PL", "POL", "+48", ".pl"),
	PORTUGAL(Continent.EUROPE, "Lissabon","PT", "PRT", "+351", ".pt"),
	PUERTO_RICO(Continent.NORTH_AMERICA, "San Juan","PR", "PRI", "+1939", ".pr"),
	QATAR(Continent.ASIA, "Doha","QA", "QAT", "+974", ".qa"),
	ROMANIA(Continent.EUROPE, "Bucarest","RO", "ROU", "+40", ".ro"),
	RUSSIAN_FEDERATION(Continent.ASIA, "Moskau","RU", "RUS", "+7", ".ru"),
	RWANDA(Continent.AFRICA, "Kigali","RW", "RWA", "+250", ".rw"),
	RÉUNION(Continent.AFRICA, "Saint-Denis","RE", "REU", "+262", ".re"),
	SAINT_HELENA(Continent.AFRICA, "Jamestown","SH", "SHN", "+290", ".sh"),
	SAINT_KITTS_AND_NEVIS(Continent.NORTH_AMERICA, "Basseterre","KN", "KNA", "+1869", ".kn"),
	SAINT_LUCIA(Continent.SOUTH_AMERICA, "Castries","LC", "LCA", "+1758", ".lc"),
	SAINT_PIERRE_AND_MIQUELON(Continent.NORTH_AMERICA, "Saint-Pierre","PM", "SPM", "+508", ".pm"),
	SAINT_VINCENT_AND_THE_GRENADINES(Continent.SOUTH_AMERICA, "Kingstown","VC", "VCT", "+1784", ".vc"),
	SAMOA(Continent.AUSTRALIA, "Apia","WS", "WSM", "", ".ws"),
	SAN_MARINO(Continent.EUROPE, "San Marino","SM", "SMR", "+378", ".sm"),
	SAO_TOME_AND_PRINCIPE(Continent.AFRICA, "S\u00e3o Tom\u00e9","ST", "STP", "+239", ".st"),
	SAUDI_ARABIA(Continent.ASIA, "Riad","SA", "SAU", "+966", ".sa"),
	SAUDI_IRAQI_NEUTRAL_ZONE(Continent.ASIA, "-/-","NT", "NTZ", "", ".nt"),
	SENEGAL(Continent.AFRICA, "Dakar","SN", "SEN", "+221", ".sn"),
	SERBIEN_UND_MONTENEGRO(Continent.EUROPE, "Belgrad","CS", "SCG", "+381", ".cs"),
	SEYCHELLES(Continent.AFRICA, "Victoria","SC", "SYC", "+248", ".sc"),
	SIERRA_LEONE(Continent.AFRICA, "Freetown","SL", "SLE", "+232", ".sl"),
	SINGAPORE(Continent.ASIA, "Singapur","SG", "SGP", "+65", ".sg"),
	SLOVAKIA(Continent.EUROPE, "Bratislava","SK", "SVK", "+421", ".sk"),
	SLOVENIA(Continent.EUROPE, "Ljubljana","SI", "SVN", "+386", ".si"),
	SOLOMON_ISLANDS(Continent.AUSTRALIA, "Honiara","SB", "SLB", "+677", ".sb"),
	SOMALIA(Continent.AFRICA, "Mogadischu","SO", "SOM", "+252", ".so"),
	SOUTH_AFRICA(Continent.AFRICA, "Tshwane / Pretoria","ZA", "ZAF", "+27", ".za"),
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS(Continent.SOUTH_AMERICA, "Grytviken","GS", "SGS", "", ""),
	SOVIET_UNION(Continent.EUROPE, "Moskau","SU", "SUN", "", ".su"),
	SPAIN(Continent.EUROPE, "Madrid","ES", "ESP", "+34", ".es"),
	SRI_LANKA(Continent.ASIA, "Colombo","LK", "LKA", "+94", ".lk"),
	SUDAN(Continent.AFRICA, "Khartum","SD", "SDN", "+249", ".sd"),
	SURINAME(Continent.SOUTH_AMERICA, "Paramaribo","SR", "SUR", "+597", ".sr"),
	SVALBARD_AND_JAN_MAYEN(Continent.EUROPE, "Longyearbyen","SJ", "SJM", "", ".sj"),
	SWAZILAND(Continent.AFRICA, "Mbabane","SZ", "SWZ", "+268", ".sz"),
	SWEDEN(Continent.EUROPE, "Stockholm","SE", "SWE", "+46", ".se"),
	SWITZERLAND(Continent.EUROPE, "Bern","CH", "CHE", "+41", ".ch"),
	SYRIAN_ARAB_REPUBLIC(Continent.ASIA, "Damaskus","SY", "SYR", "+963", ".sy"),
	TAIWAN(Continent.ASIA, "Taipeh","TW", "TWN", "+886", ".tw"),
	TAJIKISTAN(Continent.ASIA, "Duschanbe","TJ", "TJK", "+992", ".tj"),
	TANZANIA_UNITED_REPUBLIC_OF(Continent.AFRICA, "Dodoma","TZ", "TZA", "+255", ".tz"),
	THAILAND(Continent.ASIA, "Bangkok","TH", "THA", "+66", ".th"),
	TIMOR_LESTE(Continent.AUSTRALIA, "Dili","TL", "TLS", "+670", ".tl"),
	TOGO(Continent.AFRICA, "Lom\u00e9","TG", "TGO", "+228", ".tg"),
	TOKELAU(Continent.AUSTRALIA, "-/-","TK", "TKL", "+690", ".tk"),
	TONGA(Continent.AUSTRALIA, "Nuku\u2019alofa","TO", "TON", "+676", ".to"),
	TRINIDAD_AND_TOBAGO(Continent.SOUTH_AMERICA, "Port-of-Spain","TT", "TTO", "+1868", ".tt"),
	TRISTAN_DA_CUNHA(Continent.AFRICA, "Jamestown","TA", "TAA", "+290", ""),
	TUNISIA(Continent.AFRICA, "Tunis","TN", "TUN", "+216", ".tn"),
	TURKEY(Continent.ASIA, "Ankara","TR", "TUR", "+90", ".tr"),
	TURKMENISTAN(Continent.ASIA, "A\u015fgabat","TM", "TKM", "+993", ".tm"),
	TURKS_AND_CAICOS_ISLANDS(Continent.NORTH_AMERICA, "Cockburn Town auf Grand Turk","TC", "TCA", "+1649", ".tc"),
	TUVALU(Continent.AUSTRALIA, "Funafuti","TV", "TUV", "+688", ".tv"),
	UGANDA(Continent.AFRICA, "Kampala","UG", "UGA", "+256", ".ug"),
	UKRAINE(Continent.EUROPE, "Kiev","UA", "UKR", "+380", ".ua"),
	UNITED_ARAB_EMIRATES(Continent.ASIA, "Abu Dhabi","AE", "ARE", "+971", ".ae"),
	UNITED_KINGDOM(Continent.EUROPE, "London","GB", "GBR", "+44", ".gb"),
	UNITED_STATES(Continent.NORTH_AMERICA, "Washington, D.C.","US", "USA", "+1", ".us"),
	URUGUAY(Continent.SOUTH_AMERICA, "Montevideo","UY", "URY", "+598", ".uy"),
	UZBEKISTAN(Continent.ASIA, "Taschkent","UZ", "UZB", "+998", ".uz"),
	VANUATU(Continent.AUSTRALIA, "Port Vila","VU", "VUT", "+678", ".vu"),
	VENEZUELA(Continent.SOUTH_AMERICA, "Caracas","VE", "VEN", "+58", ".ve"),
	VIET_NAM(Continent.ASIA, "H\u00e0 N\u1ed9i","VN", "VNM", "+84", ".vn"),
	VIRGIN_ISLANDS_BRITISH(Continent.NORTH_AMERICA, "Road Town","VG", "VGB", "+1284", ".vg"),
	VIRGIN_ISLANDS_U_S(Continent.SOUTH_AMERICA, "Charlotte Amalie","VI", "VIR", "+1340", ".vi"),
	WALLIS_AND_FUTUNA(Continent.AUSTRALIA, "Mata-Utu","WF", "WLF", "+681", ".wf"),
	WESTERN_SAHARA(Continent.AFRICA, "El Aai\u00fan","EH", "ESH", "", ".eh"),
	YEMEN(Continent.ASIA, "Sanaa","YE", "YEM", "+967", ".ye"),
	ZAMBIA(Continent.AFRICA, "Lusaka","ZM", "ZMB", "+260", ".zm"),
	ZIMBABWE(Continent.AFRICA, "Harare","ZW", "ZWE", "+263", ".zw"),
	ÅLAND_ISLANDS(Continent.EUROPE, "Mariehamn","AX", "ALA", "+35818", ".ax");
	
	/** continent this country is situated in */
	private Continent continent;
	/** capital of this country */
	private String capital;
	/** ISO 2 country code */
	private String iso2Code;
	/** ISO 3 country code */
	private String iso3Code;
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
	private Country(Continent continent, String capital, String iso2Code, String iso3Code, String idc, String tld) {
		this.continent = continent;
		this.capital = capital;
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
