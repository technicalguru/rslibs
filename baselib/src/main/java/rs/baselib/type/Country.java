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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Countries on this planet.
 * @author ralph
 *
 */
public enum Country {

	AFGHANISTAN("Afghanistan", Continent.ASIA, "Kabul", "AF", "AFG", "004", "+93", ".af"),
	ALBANIA("Albania", Continent.EUROPE, "Tirana", "AL", "ALB", "008", "+355", ".al"),
	ALGERIA("Algeria", Continent.AFRICA, "Algier", "DZ", "DZA", "012", "+213", ".dz"),
	AMERICAN_SAMOA("American Samoa", Continent.AUSTRALIA, "Pago-Pago", "AS", "ASM", "882", "+1684", ".as"),
	ANDORRA("Andorra", Continent.EUROPE, "Andorra la Vella", "AD", "AND", null, "+376", ".ad"),
	ANGOLA("Angola", Continent.AFRICA, "Luanda", "AO", "AGO", "024", "+244", ".ao"),
	ANGUILLA("Anguilla", Continent.NORTH_AMERICA, "The Valley", "AI", "AIA", "660", "+1264", ".ai"),
	ANTARCTICA("Antarctica", Continent.ANTARCTICA, "Juneau", "AQ", "ATA", null, "+672", ".aq"),
	ANTIGUA_AND_BARBUDA("Antigua And Barbuda", Continent.NORTH_AMERICA, "Saint John's", "AG", "ATG", "028", "+1268", ".ag"),
	ARGENTINA("Argentina", Continent.SOUTH_AMERICA, "Buenos Aires", "AR", "ARG", "032", "+54", ".ar"),
	ARMENIA("Armenia", Continent.ASIA, "Eriwan", "AM", "ARM", "051", "+374", ".am"),
	ARUBA("Aruba", Continent.NORTH_AMERICA, "Oranjestad", "AW", "ABW", null, "+297", ".aw"),
	ASCENSION("Ascension", Continent.AFRICA, "-/-", "AC", "ASC", "826", "+247", ".ac"),
	AUSTRALIA("Australia", Continent.AUSTRALIA, "Canberra", "AU", "AUS", "036", "+61", ".au"),
	AUSTRIA("Austria", Continent.EUROPE, "Wien", "AT", "AUT", "040", "+43", ".at"),
	AZERBAIJAN("Azerbaijan", Continent.ASIA, "Baku", "AZ", "AZE", "031", "+994", ".az"),
	BAHAMAS("Bahamas", Continent.NORTH_AMERICA, "Nassau", "BS", "BHS", "044", "+1242", ".bs"),
	BAHRAIN("Bahrain", Continent.ASIA, "Manama", "BH", "BHR", "048", "+973", ".bh"),
	BANGLADESH("Bangladesh", Continent.ASIA, "Dhaka", "BD", "BGD", "050", "+880", ".bd"),
	BARBADOS("Barbados", Continent.NORTH_AMERICA, "Bridgetown", "BB", "BRB", "052", "+1246", ".bb"),
	BELARUS("Belarus", Continent.EUROPE, "Minsk", "BY", "BLR", "112", "+375", ".by"),
	BELGIUM("Belgium", Continent.EUROPE, "Brussels", "BE", "BEL", "056", "+32", ".be"),
	BELIZE("Belize", Continent.NORTH_AMERICA, "Belmopan", "BZ", "BLZ", "084", "+51", ".bz"),
	BENIN("Benin", Continent.AFRICA, "Porto Novo", "BJ", "BEN", "204", "+229", ".bj"),
	BERMUDA("Bermuda", Continent.NORTH_AMERICA, "Hamilton", "BM", "BMU", "060", "+1441", ".bm"),
	BHUTAN("Bhutan", Continent.ASIA, "Thimphu", "BT", "BTN", "064", "+975", ".bt"),
	BOLIVIA("Bolivia", Continent.SOUTH_AMERICA, "Sucre", "BO", "BOL", "068", "+591", ".bo"),
	BOSNIA_AND_HERZEGOVINA("Bosnia And Herzegovina", Continent.EUROPE, "Sarajevo", "BA", "BIH", "070", "+387", ".ba"),
	BOTSWANA("Botswana", Continent.AFRICA, "Gaborone", "BW", "BWA", "072", "+267", ".bw"),
	BOUVET_ISLAND("Bouvet Island", Continent.ANTARCTICA, "(Forschungsinsel)", "BV", "BVT", null, null, ".bv"),
	BRAZIL("Brazil", Continent.SOUTH_AMERICA, "Brasília", "BR", "BRA", "076", "+55", ".br"),
	BRITISH_INDIAN_OCEAN_TERRITORY("British Indian Ocean Territory", Continent.ASIA, "-/-", "IO", "IOT", "086", null, ".io"),
	BRUNEI("Brunei", Continent.ASIA, "Bandar Seri Begawan", "BN", "BRN", "096", "+673", ".bn"),
	BULGARIA("Bulgaria", Continent.EUROPE, "Sofia", "BG", "BGR", "100", "+359", ".bg"),
	BURKINA_FASO("Burkina Faso", Continent.AFRICA, "Ouagadougou", "BF", "BFA", "854", "+226", ".bf"),
	BURUNDI("Burundi", Continent.AFRICA, "Bujumbura", "BI", "BDI", "108", "+257", ".bi"),
	CAMBODIA("Cambodia", Continent.ASIA, "Phnom Penh", "KH", "KHM", "116", "+855", ".kh"),
	CAMEROON("Cameroon", Continent.AFRICA, "Yaoundé", "CM", "CMR", "120", "+237", ".cm"),
	CANADA("Canada", Continent.NORTH_AMERICA, "Ottawa", "CA", "CAN", "124", "+1NXX", ".ca"),
	CANARY_ISLANDS("Canary Islands", Continent.EUROPE, "Santa Cruz", "IC", null, "724", null, null),
	CAPE_VERDE("Cape Verde", Continent.AFRICA, "Praia", "CV", "CPV", "132", "+238", ".cv"),
	CAYMAN_ISLANDS("Cayman Islands", Continent.NORTH_AMERICA, "George Town", "KY", "CYM", "136", "+1345", ".ky"),
	CENTRAL_AFRICAN_REPUBLIC("Central African Republic", Continent.AFRICA, "Bangui", "CF", "CAF", "140", "+236", ".cf"),
	CHAD("Chad", Continent.AFRICA, "N'Djamena", "TD", "TCD", "148", "+235", ".td"),
	CHILE("Chile", Continent.SOUTH_AMERICA, "Santiago", "CL", "CHL", "152", "+56", ".cl"),
	CHINA("China", Continent.ASIA, "Peking (Beijing)", "CN", "CHN", "156", "+86", ".cn"),
	CHRISTMAS_ISLAND("Christmas Island", Continent.ASIA, "Flying Fish Cove", "CX", "CXR", null, null, ".cx"),
	COCOS_KEELING_ISLANDS("Cocos Keeling Islands", Continent.ASIA, "West Island", "CC", "CCK", null, null, ".cc"),
	COLOMBIA("Colombia", Continent.SOUTH_AMERICA, "Santa Fé de Bogotá", "CO", "COL", "170", "+57", ".co"),
	COMOROS("Comoros", Continent.AFRICA, "Moroni", "KM", null, "450", "+269", ".km"),
	CONGO("Congo", Continent.AFRICA, "Brazzaville", "CG", "COG", "178", "+242", ".cg"),
	CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE("The Democratic Republic Of The Congo", Continent.AFRICA, "Kinshasa", "CD", "COD", "180", "+243", ".cd"),
	COOK_ISLANDS("Cook Islands", Continent.AUSTRALIA, "Avarua", "CK", "COK", "184", "+682", ".ck"),
	COSTA_RICA("Costa Rica", Continent.NORTH_AMERICA, "San José", "CR", "CRI", "188", "+56", ".cr"),
	CROATIA("Croatia", Continent.EUROPE, "Zagreb", "HR", "HRV", "191", "+385", ".hr"),
	CUBA("Cuba", Continent.NORTH_AMERICA, "Havanna", "CU", "CUB", "192", "+53", ".cu"),
	CYPRUS("Cyprus", Continent.ASIA, "Nikosia", "CY", "CYP", "196", "+357", ".cy"),
	CZECH_REPUBLIC("Czech Republic", Continent.EUROPE, "Prag", "CZ", "CZE", "203", "+420", ".cz"),
	IVORY_COAST("Ivory Coast", Continent.AFRICA, "Yamoussoukro", "CI", "CIV", "384", "+225", ".ci"),
	DENMARK("Denmark", Continent.EUROPE, "Kopenhagen", "DK", "DNK", "208", "+45", ".dk"),
	DIEGO_GARCIA("Diego Garcia", Continent.AFRICA, "Diego Garcia (Main Island)", "DG", "DGA", null, "+246", "-/-"),
	DJIBOUTI("Djibouti", Continent.AFRICA, "Dschibuti", "DJ", "DJI", "262", "+253", "dj"),
	DOMINICA("Dominica", Continent.NORTH_AMERICA, "Roseau", "DM", "DMA", "212", "+1767", ".dm"),
	DOMINICAN_REPUBLIC("Dominican Republic", Continent.SOUTH_AMERICA, "Santo Domingo", "DO", "DOM", "214", "+1809", ".do"),
	ENGLAND("England", Continent.EUROPE, "London", "ENG", "ENG", null, "+44", null),
	ECUADOR("Ecuador", Continent.SOUTH_AMERICA, "Quito", "EC", "ECU", "218", "+593", ".ec"),
	EGYPT("Egypt", Continent.AFRICA, "Kairo", "EG", "EGY", "818", "+20", ".eg"),
	EL_SALVADOR("El Salvador", Continent.NORTH_AMERICA, "San Salvador", "SV", "SLV", "222", "+53", ".sv"),
	EQUATORIAL_GUINEA("Equatorial Guinea", Continent.AFRICA, "Malabo", "GQ", "GNQ", "226", "+240", ".gq"),
	ERITREA("Eritrea", Continent.AFRICA, "Asmara (Asmera)", "ER", "ERI", "232", "+291", ".er"),
	ESTONIA("Estonia", Continent.EUROPE, "Tallinn (Reval)", "EE", "EST", "233", "+372", ".ee"),
	ETHIOPIA("Ethiopia", Continent.AFRICA, "Addis Abeba", "ET", "ETH", "231", "+251", ".et"),
	EUROPEAN_UNION("European Union", Continent.EUROPE, "Brussels", "EU", "-/-", null, "+3883", ".eu"),
	FALKLAND_ISLANDS("Falkland Islands", Continent.AFRICA, "Port Stanley", "FK", "FLK", "238", "+500", ".fk"),
	FAROE_ISLANDS("Faroe Islands", Continent.EUROPE, "Tórshavn", "FO", "FRO", null, "+298", ".fo"),
	FIJI("Fiji", Continent.AUSTRALIA, "Suva", "FJ", "FJI", "242", "+679", ".fj"),
	FINLAND("Finland", Continent.EUROPE, "Helsinki", "FI", "FIN", "246", "+358", ".fi"),
	FRANCE("France", Continent.EUROPE, "Paris", "FR", "FRA", "250", "+33", ".fr"),
	FRENCH_GUIANA("French Guiana", Continent.SOUTH_AMERICA, "Cayenne", "GF", "GUF", "254", "+594", ".gf"),
	FRENCH_POLYNESIA("French Polynesia", Continent.AUSTRALIA, "Papeete", "PF", "PYF", "258", "+689", ".pf"),
	FRENCH_SOUTHERN_TERRITORIES("French Southern Territories", Continent.ANTARCTICA, "Port-aux-Français", "TF", "ATF", "250", null, ".tf"),
	GABON("Gabon", Continent.AFRICA, "Libreville", "GA", "GAB", "266", "+241", ".ga"),
	GAMBIA("Gambia", Continent.AFRICA, "Banjul", "GM", "GMB", "270", "+220", ".gm"),
	GEORGIA("Georgia", Continent.EUROPE, "Tiflis", "GE", "GEO", "268", "+995", ".ge"),
	GERMANY("Germany", Continent.EUROPE, "Berlin", "DE", "DEU", "276", "+49", ".de"),
	GHANA("Ghana", Continent.AFRICA, "Accra", "GH", "GHA", "288", "+233", ".gh"),
	GIBRALTAR("Gibraltar", Continent.AFRICA, "Gibraltar", "GI", "GIB", "384", "+350", ".gi"),
	GREECE("Greece", Continent.EUROPE, "Athen", "GR", "GRC", "300", "+30", ".gr"),
	GREENLAND("Greenland", Continent.NORTH_AMERICA, "Nuuk", "GL", "GRL", "304", "+299", ".gl"),
	GRENADA("Grenada", Continent.NORTH_AMERICA, "St. George's", "GD", "GRD", "308", "+1473", ".gd"),
	GUADELOUPE("Guadeloupe", Continent.NORTH_AMERICA, "Basse-Terre", "GP", "GLP", null, "+590", ".gp"),
	GUAM("Guam", Continent.ASIA, "Hagåtña", "GU", "GUM", null, "+1671", ".gu"),
	GUATEMALA("Guatemala", Continent.NORTH_AMERICA, "Guatemala City", "GT", "GTM", "320", "+52", ".gt"),
	GUERNSEY("Guernsey", Continent.EUROPE, "St. Peter Port", "GG", "GGY", null, "+44", ".gg"),
	GUINEA("Guinea", Continent.AFRICA, "Conakry", "GN", "GIN", "324", "+224", ".gn"),
	GUINEA_BISSAU("Guinea Bissau", Continent.AFRICA, "Bissau", "GW", "GNB", "624", "+245", ".gw"),
	GUYANA("Guyana", Continent.SOUTH_AMERICA, "Georgetown", "GY", "GUY", "328", "+592", ".gy"),
	HAITI("Haiti", Continent.NORTH_AMERICA, "Port-au-Prince", "HT", "HTI", "332", "+59", ".ht"),
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("Heard Island And McDonald Islands", Continent.AUSTRALIA, null, "HM", "HMD", null, null, ".hm"),
	VATICAN("The Vatican", Continent.EUROPE, "Vatikan City", "VA", "VAT", null, "+3906", ".va"),
	HONDURAS("Honduras", Continent.NORTH_AMERICA, "Tegucigalpa", "HN", "HND", "340", "+54", ".hn"),
	HONG_KONG("Hong Kong", Continent.ASIA, "-/-", "HK", "HKG", "344", "+852", ".hk"),
	HUNGARY("Hungary", Continent.EUROPE, "Budapest", "HU", "HUN", "348", "+36", ".hu"),
	ICELAND("Iceland", Continent.EUROPE, "Reykjavík", "IS", "ISL", "352", "+354", ".is"),
	INDIA("India", Continent.ASIA, "Neu-Delhi", "IN", "IND", "356", "+91", ".in"),
	INDONESIA("Indonesia", Continent.ASIA, "Jakarta", "ID", "IDN", "360", "+62", ".id"),
	IRAN_ISLAMIC_REPUBLIC_OF("Islamic Republic Of Iran", Continent.ASIA, "Teheran", "IR", "IRN", "364", "+98", ".ir"),
	IRAQ("Iraq", Continent.ASIA, "Bagdad", "IQ", "IRQ", "368", "+964", ".iq"),
	IRELAND("Ireland", Continent.EUROPE, "Dublin", "IE", "IRL", "372", "+353", ".ie"),
	ISLE_OF_MAN("Isle Of Man", Continent.EUROPE, "Douglas", "IM", "IMN", null, "+44", ".im"),
	ISRAEL("Israel", Continent.ASIA, "Jerusalem", "IL", "ISR", "376", "+972", ".il"),
	ITALY("Italy", Continent.EUROPE, "Rom", "IT", "ITA", "380", "+39", ".it"),
	JAMAICA("Jamaica", Continent.NORTH_AMERICA, "Kingston", "JM", "JAM", "388", "+1876", ".jm"),
	JAPAN("Japan", Continent.ASIA, "Tokio", "JP", "JPN", "392", "+81", ".jp"),
	JERSEY("Jersey", Continent.EUROPE, "Saint Helier", "JE", "JEY", null, "+44", ".je"),
	JORDAN("Jordan", Continent.ASIA, "Amman", "JO", "JOR", "400", "+962", ".jo"),
	KAZAKHSTAN("Kazakhstan", Continent.ASIA, "Astana", "KZ", "KAZ", "398", "+7", ".kz"),
	KENYA("Kenya", Continent.AFRICA, "Nairobi", "KE", "KEN", "404", "+254", ".ke"),
	KIRIBATI("Kiribati", Continent.AUSTRALIA, "Bairiki", "KI", "KIR", "296", "+686", ".ki"),
	KOREA_DEMOCRATIC_PEOPLE_S_REPUBLIC_OF("Democratic People's Republic of Korea", Continent.ASIA, "P'yŏngyang", "KP", "PRK", "408", "+850", ".kp"),
	KOREA_REPUBLIC_OF("Republic Of Korea", Continent.ASIA, "Seoul", "KR", "KOR", "410", "+82", ".kr"),
	KUWAIT("Kuwait", Continent.ASIA, "Kuwait", "KW", "KWT", "414", "+965", ".kw"),
	KYRGYZSTAN("Kyrgyzstan", Continent.ASIA, "Bischkek", "KG", "KGZ", null, "+996", ".kg"),
	LAO_PEOPLE_S_DEMOCRATIC_REPUBLIC("Lao People's Democratic Republic", Continent.ASIA, "Vientiane", "LA", "LAO", "418", "+856", ".la"),
	LATVIA("Latvia", Continent.EUROPE, "Rīga", "LV", "LVA", "428", "+371", ".lv"),
	LEBANON("Lebanon", Continent.ASIA, "Beirut", "LB", "LBN", "422", "+961", ".lb"),
	LESOTHO("Lesotho", Continent.AFRICA, "Maseru", "LS", "LSO", "426", "+266", ".ls"),
	LIBERIA("Liberia", Continent.AFRICA, "Monrovia", "LR", "LBR", "430", "+231", ".lr"),
	LIBYAN_ARAB_JAMAHIRIYA("Libyan Arab Jamahiriya", Continent.AFRICA, "Tripolis", "LY", "LBY", "434", "+218", ".ly"),
	LIECHTENSTEIN("Liechtenstein", Continent.EUROPE, "Vaduz", "LI", "LIE", null, "+423", ".li"),
	LITHUANIA("Lithuania", Continent.EUROPE, "Wilna", "LT", "LTU", "440", "+370", ".lt"),
	LUXEMBOURG("Luxembourg", Continent.EUROPE, "Luxemburg", "LU", "LUX", "442", "+352", ".lu"),
	MACAO("Macao", Continent.ASIA, "-/-", "MO", "MAC", "446", "+853", ".mo"),
	MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF("The Former Yugoslav Republic OfMacedonia", Continent.EUROPE, "Skopje", "MK", "MKD", "807", "+389", ".mk"),
	MADAGASCAR("Madagascar", Continent.AFRICA, "Antananarivo", "MG", "MDG", "450", "+261", ".mg"),
	MALAWI("Malawi", Continent.AFRICA, "Lilongwe", "MW", "MWI", "454", "+265", ".mw"),
	MALAYSIA("Malaysia", Continent.ASIA, "Kuala Lumpur", "MY", "MYS", "458", "+60", ".my"),
	MALDIVES("Maldives", Continent.ASIA, "Malé", "MV", "MDV", "462", "+960", ".mv"),
	MALI("Mali", Continent.AFRICA, "Bamako", "ML", "MLI", "466", "+223", ".ml"),
	MALTA("Malta", Continent.EUROPE, "Valletta", "MT", "MLT", "470", "+356", ".mt"),
	MARSHALL_ISLANDS("Marshall Islands", Continent.AUSTRALIA, "Delap-Uliga-Darrit", "MH", "MHL", "584", "+692", ".mh"),
	MARTINIQUE("Martinique", Continent.NORTH_AMERICA, "Fort-de-France", "MQ", "MTQ", null, "+596", ".mq"),
	MAURITANIA("Mauritania", Continent.AFRICA, "Nouakchott", "MR", "MRT", "478", "+222", ".mr"),
	MAURITIUS("Mauritius", Continent.AFRICA, "Port Louis", "MU", "MUS", "480", "+230", ".mu"),
	MAYOTTE("Mayotte", Continent.AFRICA, "Mamoudzou", "YT", "MYT", null, "+269", ".yt"),
	MEXICO("Mexico", Continent.NORTH_AMERICA, "Mexico City", "MX", "MEX", "484", "+52", ".mx"),
	MICRONESIA_FEDERATED_STATES_OF("Federated States Of Micronesia", Continent.AUSTRALIA, "Palikir", "FM", "FSM", "583", "+691", ".fm"),
	MOLDOVA("Moldova", Continent.EUROPE, "Chişinău", "MD", "MDA", "498", "+373", ".md"),
	MONACO("Monaco", Continent.EUROPE, "Monaco", "MC", "MCO", null, "+377", ".mc"),
	MONGOLIA("Mongolia", Continent.ASIA, "Ulaanbaatar", "MN", "MNG", "496", "+976", ".mn"),
	MONTENEGRO("Montenegro", Continent.EUROPE, "Podgorica", "ME", "MNE", "090", "+382", ".me"),
	MONTSERRAT("Montserrat", Continent.NORTH_AMERICA, "Plymouth", "MS", "MSR", "500", "+1664", ".ms"),
	MOROCCO("Morocco", Continent.AFRICA, "Rabat", "MA", "MAR", "504", "+212", ".ma"),
	MOZAMBIQUE("Mozambique", Continent.AFRICA, "Maputo", "MZ", "MOZ", "508", "+258", ".mz"),
	MYANMAR("Myanmar", Continent.ASIA, "Rangun", "MM", "MMR", "104", "+95", ".mm"),
	NAMIBIA("Namibia", Continent.AFRICA, "Windhoek", "NA", "NAM", "516", "+264", ".na"),
	NAURU("Nauru", Continent.AUSTRALIA, "Yaren", "NR", "NRU", "520", "+674", ".nr"),
	NEPAL("Nepal", Continent.ASIA, "Kathmandu", "NP", "NPL", "524", "+977", ".np"),
	NETHERLANDS("Netherlands", Continent.EUROPE, "Amsterdam", "NL", "NLD", "528", "+31", ".nl"),
	NETHERLANDS_ANTILLES("Netherlands Antilles", Continent.NORTH_AMERICA, "Willemstad", "AN", "ANT", "530", "+599", ".an"),
	NEW_CALEDONIA("New Caledonia", Continent.AUSTRALIA, "Nouméa", "NC", "NCL", "540", "+687", ".nc"),
	NEW_ZEALAND("New Zealand", Continent.AUSTRALIA, "Wellington", "NZ", "NZL", "554", "+64", ".nz"),
	NICARAGUA("Nicaragua", Continent.NORTH_AMERICA, "Managua", "NI", "NIC", "558", "+55", ".ni"),
	NIGER("Niger", Continent.AFRICA, "Niamey", "NE", "NER", "562", "+227", ".ne"),
	NIGERIA("Nigeria", Continent.AFRICA, "Abuja", "NG", "NGA", "566", "+234", ".ng"),
	NIUE("Niue", Continent.AUSTRALIA, "Alofi", "NU", "NIU", "570", "+683", ".nu"),
	NORFOLK_ISLAND("Norfolk Island", Continent.AUSTRALIA, "Kingston", "NF", "NFK", null, "+6723", ".nf"),
	NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", Continent.AUSTRALIA, "Saipan", "MP", "MNP", "840", "+1670", ".mp"),
	NORWAY("Norway", Continent.EUROPE, "Oslo", "NO", "NOR", "578", "+47", ".no"),
	OMAN("Oman", Continent.ASIA, "Maskat", "OM", "OMN", "512", "+968", ".om"),
	PAKISTAN("Pakistan", Continent.ASIA, "Islamabad", "PK", "PAK", "586", "+92", ".pk"),
	PALAU("Palau", Continent.AUSTRALIA, "Melekeok", "PW", "PLW", null, "+680", ".pw"),
	PALESTINIAN_TERRITORY_OCCUPIED("Occupied Palestinian Territory", Continent.ASIA, "Ramallah", "PS", "PSE", null, "+970", ".ps"),
	PANAMA("Panama", Continent.SOUTH_AMERICA, "Panama City", "PA", "PAN", "591", "+57", ".pa"),
	PAPUA_NEW_GUINEA("Papua New Guinea", Continent.AUSTRALIA, "Port Moresby", "PG", "PNG", "598", "+675", ".pg"),
	PARAGUAY("Paraguay", Continent.SOUTH_AMERICA, "Asunción", "PY", "PRY", "600", "+595", ".py"),
	PERU("Peru", Continent.SOUTH_AMERICA, "Lima", "PE", "PER", "604", "+51", ".pe"),
	PHILIPPINES("Philippines", Continent.ASIA, "Manila", "PH", "PHL", "608", "+63", ".ph"),
	PITCAIRN("Pitcairn", Continent.AUSTRALIA, "Adamstown", "PN", "PCN", null, "+649", ".pn"),
	POLAND("Poland", Continent.EUROPE, "Warszaw", "PL", "POL", "616", "+48", ".pl"),
	PORTUGAL("Portugal", Continent.EUROPE, "Lissabon", "PT", "PRT", "620", "+351", ".pt"),
	PUERTO_RICO("Puerto Rico", Continent.NORTH_AMERICA, "San Juan", "PR", "PRI", "630", "+1939", ".pr"),
	QATAR("Qatar", Continent.ASIA, "Doha", "QA", "QAT", "634", "+974", ".qa"),
	ROMANIA("Romania", Continent.EUROPE, "Bucarest", "RO", "ROU", "642", "+40", ".ro"),
	RUSSIAN_FEDERATION("Russian Federation", Continent.ASIA, "Moskau", "RU", "RUS", "643", "+7", ".ru"),
	RWANDA("Rwanda", Continent.AFRICA, "Kigali", "RW", "RWA", "646", "+250", ".rw"),
	RÉUNION("Réunion", Continent.AFRICA, "Saint-Denis", "RE", "REU", null, "+262", ".re"),
	SAINT_HELENA("Saint Helena", Continent.AFRICA, "Jamestown", "SH", "SHN", null, "+290", ".sh"),
	SAINT_KITTS_AND_NEVIS("Saint Kitts And Nevis", Continent.NORTH_AMERICA, "Basseterre", "KN", "KNA", "659", "+1869", ".kn"),
	SAINT_LUCIA("Saint Lucia", Continent.SOUTH_AMERICA, "Castries", "LC", "LCA", "662", "+1758", ".lc"),
	SAINT_PIERRE_AND_MIQUELON("Saint Pierre And Miquelon", Continent.NORTH_AMERICA, "Saint-Pierre", "PM", "SPM", null, "+508", ".pm"),
	SAINT_VINCENT_AND_THE_GRENADINES("Saint Vincent And The Grenadines", Continent.SOUTH_AMERICA, "Kingstown", "VC", "VCT", "670", "+1784", ".vc"),
	SAMOA("Samoa", Continent.AUSTRALIA, "Apia", "WS", "WSM", "882", null, ".ws"),
	SAN_MARINO("San Marino", Continent.EUROPE, "San Marino", "SM", "SMR", null, "+378", ".sm"),
	SAO_TOME_AND_PRINCIPE("Sao Tome And Principe", Continent.AFRICA, "São Tomé", "ST", "STP", "678", "+239", ".st"),
	SAUDI_ARABIA("Saudi Arabia", Continent.ASIA, "Riad", "SA", "SAU", "682", "+966", ".sa"),
	SAUDI_IRAQI_NEUTRAL_ZONE("Saudi Iraqi Neutral Zone", Continent.ASIA, "-/-", "NT", "NTZ", null, null, ".nt"),
	SCOTLAND("Scotland", Continent.EUROPE, "Edinburgh", "SCO", "SCO", null, "+44", null),
	SENEGAL("Senegal", Continent.AFRICA, "Dakar", "SN", "SEN", "686", "+221", ".sn"),
	SERBIA("Serbia", Continent.EUROPE, "Belgrad", "RS", "SRB", "381", "+381", ".rs"),
	SERBIEN_UND_MONTENEGRO("Serbien Und Montenegro", Continent.EUROPE, "Belgrad", "CS", "SCG", "090", "+381", ".cs"),
	SEYCHELLES("Seychelles", Continent.AFRICA, "Victoria", "SC", "SYC", "690", "+248", ".sc"),
	SIERRA_LEONE("Sierra Leone", Continent.AFRICA, "Freetown", "SL", "SLE", "694", "+232", ".sl"),
	SINGAPORE("Singapore", Continent.ASIA, "Singapur", "SG", "SGP", "702", "+65", ".sg"),
	SLOVAKIA("Slovakia", Continent.EUROPE, "Bratislava", "SK", "SVK", "703", "+421", ".sk"),
	SLOVENIA("Slovenia", Continent.EUROPE, "Ljubljana", "SI", "SVN", "705", "+386", ".si"),
	SOLOMON_ISLANDS("Solomon Islands", Continent.AUSTRALIA, "Honiara", "SB", "SLB", "090", "+677", ".sb"),
	SOMALIA("Somalia", Continent.AFRICA, "Mogadischu", "SO", "SOM", "706", "+252", ".so"),
	SOUTH_AFRICA("South Africa", Continent.AFRICA, "Tshwane / Pretoria", "ZA", "ZAF", "710", "+27", ".za"),
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("South Georgia And The South Sandwich Islands", Continent.SOUTH_AMERICA, "Grytviken", "GS", "SGS", null, null, null),
	SOVIET_UNION("Soviet Union", Continent.EUROPE, "Moskau", "SU", "SUN", null, null, ".su"),
	SPAIN("Spain", Continent.EUROPE, "Madrid", "ES", "ESP", "724", "+34", ".es"),
	SRI_LANKA("Sri Lanka", Continent.ASIA, "Colombo", "LK", "LKA", "144", "+94", ".lk"),
	SUDAN("Sudan", Continent.AFRICA, "Khartum", "SD", "SDN", "736", "+249", ".sd"),
	SURINAME("Suriname", Continent.SOUTH_AMERICA, "Paramaribo", "SR", "SUR", "740", "+597", ".sr"),
	SVALBARD_AND_JAN_MAYEN("Svalbard And Jan Mayen", Continent.EUROPE, "Longyearbyen", "SJ", "SJM", null, null, ".sj"),
	SWAZILAND("Swaziland", Continent.AFRICA, "Mbabane", "SZ", "SWZ", "748", "+268", ".sz"),
	SWEDEN("Sweden", Continent.EUROPE, "Stockholm", "SE", "SWE", "752", "+46", ".se"),
	SWITZERLAND("Switzerland", Continent.EUROPE, "Bern", "CH", "CHE", "756", "+41", ".ch"),
	SYRIAN_ARAB_REPUBLIC("Syrian Arab Republic", Continent.ASIA, "Damaskus", "SY", "SYR", "760", "+963", ".sy"),
	TAIWAN("Taiwan", Continent.ASIA, "Taipeh", "TW", "TWN", "158", "+886", ".tw"),
	TAJIKISTAN("Tajikistan", Continent.ASIA, "Duschanbe", "TJ", "TJK", null, "+992", ".tj"),
	TANZANIA_UNITED_REPUBLIC_OF("United Republic Of Tanzania", Continent.AFRICA, "Dodoma", "TZ", "TZA", "834", "+255", ".tz"),
	THAILAND("Thailand", Continent.ASIA, "Bangkok", "TH", "THA", "764", "+66", ".th"),
	TIMOR_LESTE("Timor Leste", Continent.AUSTRALIA, "Dili", "TL", "TLS", null, "+670", ".tl"),
	TOGO("Togo", Continent.AFRICA, "Lomé", "TG", "TGO", "768", "+228", ".tg"),
	TOKELAU("Tokelau", Continent.AUSTRALIA, "-/-", "TK", "TKL", null, "+690", ".tk"),
	TONGA("Tonga", Continent.AUSTRALIA, "Nuku’alofa", "TO", "TON", null, "+676", ".to"),
	TRINIDAD_AND_TOBAGO("Trinidad And Tobago", Continent.SOUTH_AMERICA, "Port-of-Spain", "TT", "TTO", "780", "+1868", ".tt"),
	TRISTAN_DA_CUNHA("Tristan Da Cunha", Continent.AFRICA, "Jamestown", "TA", "TAA", null, "+290", null),
	TUNISIA("Tunisia", Continent.AFRICA, "Tunis", "TN", "TUN", "788", "+216", ".tn"),
	TURKEY("Turkey", Continent.ASIA, "Ankara", "TR", "TUR", "792", "+90", ".tr"),
	TURKMENISTAN("Turkmenistan", Continent.ASIA, "Aşgabat", "TM", "TKM", null, "+993", ".tm"),
	TURKS_AND_CAICOS_ISLANDS("Turks And Caicos Islands", Continent.NORTH_AMERICA, "Cockburn Town auf Grand Turk", "TC", "TCA", "796", "+1649", ".tc"),
	TUVALU("Tuvalu", Continent.AUSTRALIA, "Funafuti", "TV", "TUV", null, "+688", ".tv"),
	UGANDA("Uganda", Continent.AFRICA, "Kampala", "UG", "UGA", "800", "+256", ".ug"),
	UKRAINE("Ukraine", Continent.EUROPE, "Kiev", "UA", "UKR", "804", "+380", ".ua"),
	UNITED_ARAB_EMIRATES("United Arab Emirates", Continent.ASIA, "Abu Dhabi", "AE", "ARE", "784", "+971", ".ae"),
	UNITED_KINGDOM("United Kingdom", Continent.EUROPE, "London", "GB", "GBR", "826", "+44", ".gb"),
	UNITED_STATES("United States", Continent.NORTH_AMERICA, "Washington, D.C.", "US", "USA", "840", "+1", ".us"),
	URUGUAY("Uruguay", Continent.SOUTH_AMERICA, "Montevideo", "UY", "URY", "858", "+598", ".uy"),
	UZBEKISTAN("Uzbekistan", Continent.ASIA, "Taschkent", "UZ", "UZB", "860", "+998", ".uz"),
	VANUATU("Vanuatu", Continent.AUSTRALIA, "Port Vila", "VU", "VUT", "548", "+678", ".vu"),
	VENEZUELA("Venezuela", Continent.SOUTH_AMERICA, "Caracas", "VE", "VEN", "862", "+58", ".ve"),
	VIET_NAM("Viet Nam", Continent.ASIA, "Hà Nội", "VN", "VNM", "704", "+84", ".vn"),
	VIRGIN_ISLANDS_BRITISH("British Virgin Islands", Continent.NORTH_AMERICA, "Road Town", "VG", "VGB", "092", "+1284", ".vg"),
	VIRGIN_ISLANDS_US("US Virgin Islands", Continent.SOUTH_AMERICA, "Charlotte Amalie", "VI", "VIR", "850", "+1340", ".vi"),
	WALES("Wales", Continent.EUROPE, "Cardiff", "WAL", "WAL", null, "+44", null),
	WALLIS_AND_FUTUNA("Wallis And Futuna", Continent.AUSTRALIA, "Mata-Utu", "WF", "WLF", "876", "+681", ".wf"),
	WESTERN_SAHARA("Western Sahara", Continent.AFRICA, "El Aaiún", "EH", "ESH", null, null, ".eh"),
	YEMEN("Yemen", Continent.ASIA, "Sanaa", "YE", "YEM", "887", "+967", ".ye"),
	ZAMBIA("Zambia", Continent.AFRICA, "Lusaka", "ZM", "ZMB", "894", "+260", ".zm"),
	ZIMBABWE("Zimbabwe", Continent.AFRICA, "Harare", "ZW", "ZWE", "716", "+263", ".zw"),
	ÅLAND_ISLANDS("Åland Islands", Continent.EUROPE, "Mariehamn", "AX", "ALA", null, "+35818", ".ax");
		
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_AFRICA        = getCountries(Continent.AFRICA);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_ANTARCTICA    = getCountries(Continent.ANTARCTICA);
	/** All countries in continent / Arctic is empty right now */
	// public static EnumSet<Country> COUNTRIES_ARCTICA       = getCountries(Continent.ARCTICA);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_ASIA          = getCountries(Continent.ASIA);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_AUSTRALIA     = getCountries(Continent.AUSTRALIA);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_EUROPE        = getCountries(Continent.EUROPE);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_NORTH_AMERICA = getCountries(Continent.NORTH_AMERICA);
	/** All countries in continent */
	public static EnumSet<Country> COUNTRIES_SOUTH_AMERICA = getCountries(Continent.SOUTH_AMERICA);

	/** The name */
	private String name;
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
	private Country(String name, Continent continent, String capital, String iso2Code, String iso3Code, String iso3DigitCode, String idc, String tld) {
		this.name = name;
		this.continent = continent;
		this.capital = capital;
		this.iso2Code = iso2Code;
		this.iso3Code = iso3Code;
		this.iso3DigitCode = iso3DigitCode;
		this.idc = idc;
		this.tld = tld;
	}
	
	/**
	 * Returns the name of the country for display.
	 * @return the country name
	 */
	public String getName() {
		return name;
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
	
	public String toFullString() {
		return name()+" [name="+name+", continent="+continent+", capital="+capital+", iso2Code="+iso2Code+", "
				+ "iso3Code="+iso3Code+", iso3DigitCode"+iso3DigitCode+", idc="+idc+", tld="+tld+"]";
	}

	/**
	 * Returns the countries of the given continent.
	 * @param continent continent to check for
	 * @return the countries of that continent
	 */
	private static EnumSet<Country> getCountries(Continent continent) {
		Collection<Country> rc = new ArrayList<>();
		for (Country country : Country.values()) {
			if (continent.equals(country.getContinent())) rc.add(country);
		}
		//if (rc.isEmpty()) throw new RuntimeException(continent.name()+" is empty");
		return EnumSet.copyOf(rc);
	}
	
}
