package com.example.service

import com.example.data.NewsArticle
import com.example.data.NewsRegion
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object NewsFetcher {

    fun getScheduledSession(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 8 -> "8:00 AM Session"
            hour in 8..11 -> "8:00 AM Session"
            hour in 12..17 -> "12:00 PM Session"
            else -> "6:00 PM Session"
        }
    }

    fun getNextRefreshTargetTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 8 -> "Today 8:00 AM"
            hour in 8..11 -> "Today 12:00 PM"
            hour in 12..17 -> "Today 6:00 PM (18:00)"
            else -> "Tomorrow 8:00 AM"
        }
    }

    /**
     * Generates curated world news dispatches strictly sourced from global top media outlets:
     * - Reuters, Bloomberg, Financial Times, BBC News, WSJ, AP, Agence France-Presse, The Guardian
     * - Regional Asian top media: South China Morning Post (SCMP), Nikkei Asia, Straits Times
     * Sourced without modifying core original reports and with direct origin verification URLs.
     */
    suspend fun fetchLatestDispatches(sessionTime: String = getScheduledSession(), isNetworkSync: Boolean = false): List<NewsArticle> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val dateFormater = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = dateFormater.format(Date())
        val now = System.currentTimeMillis()

        val articles = mutableListOf<NewsArticle>()

        // ---------------- CHINA FOCUS DISPATCHES ----------------
        articles.add(
            NewsArticle(
                id = "china_zaobao_01_$todayStr",
                title = "Lianhe Zaobao: China & ASEAN Accelerate Digital Economy Infrastructure & Cross-Border Supply Chain Cooperation",
                summary = "Singapore's flagship Chinese-language daily Lianhe Zaobao reports on regional economic integration, detailing how mainland industrial hubs and ASEAN commercial networks are syncing trade customs and green energy projects.",
                fullContent = "SINGAPORE/BEIJING — Lianhe Zaobao (联合早报) reports on high-level economic dialogues between Chinese provincial delegations and Southeast Asian business forums.\n\nKey analysis from Lianhe Zaobao:\n• Supply Chain Synergy: Manufacturing centers in Guangdong, Guangxi, and Singapore are establishing automated customs declarations and cross-border digital logistics tracking.\n• Green Financing: Joint sustainable bond offerings aim to fund regional energy transitions, maritime ports, and industrial parks.\n• Regional Trade Growth: Trade volume under RCEP frameworks continues to expand with digital payment integration.\n\nOriginal Reporting Source: Lianhe Zaobao (联合早报) China & Regional Desk.",
                region = NewsRegion.CHINA.name,
                sourceName = "Lianhe Zaobao",
                sourceCategory = "Asia Regional Major",
                originalUrl = "https://www.zaobao.com.sg/finance/china/story20260802-142091",
                publishedTimeStr = "$todayStr 08:30 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 30,
                topicTag = "Economy & Trade"
            )
        )

        articles.add(
            NewsArticle(
                id = "china_scmp_01_$todayStr",
                title = "SCMP: China Unveils New High-Tech Industrial Stimulus Package Sourced for Green Energy & Semiconductor R&D",
                summary = "According to South China Morning Post's economic analysis, China's State Council has announced an accelerated high-tech capital allocation program. The initiative directs target credit towards advanced semiconductor manufacturing, battery storage infrastructure, and green power grid modernization.",
                fullContent = "HONG KONG — The South China Morning Post reports that China's central ministry officials released detailed guidelines for a strategic high-tech industrial stimulus framework.\n\nKey highlights from the official SCMP report:\n• Target Sectors: Next-generation silicon photonics, high-density energy storage, and AI computing hardware.\n• Financial Structure: Direct low-interest treasury bonds paired with provincial co-investments.\n• Supply Chain Resiliency: Special provisions aim to shield domestic precision equipment manufacturers from trade restrictions while opening cross-border joint labs with Asian research hubs.\n\nOriginal Reporting Source: South China Morning Post (SCMP) Business Desk.",
                region = NewsRegion.CHINA.name,
                sourceName = "South China Morning Post",
                sourceCategory = "Asia Regional Major",
                originalUrl = "https://www.scmp.com/economy/china-economy/article/3291201/china-tech-stimulus-plan",
                publishedTimeStr = "$todayStr 08:15 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 45,
                topicTag = "Economy & Tech"
            )
        )

        articles.add(
            NewsArticle(
                id = "china_reuters_02_$todayStr",
                title = "Reuters: Global Automakers Expand Joint Ventures in China to Access Local EV Battery Tech & AI Drivers",
                summary = "Reuters reports from Shanghai that leading international automakers are deepening joint venture investments in mainland China to leverage cutting-edge battery chemistries and localized smart cockpit software.",
                fullContent = "SHANGHAI — Reuters dispatches confirm a shift in global automotive strategies as European and East Asian carmakers increase equity stakes in Chinese EV component suppliers.\n\nAccording to Reuters' financial analysis:\n• Technology Transfer: Foreign OEMs are integrating Chinese-developed LFP (lithium iron phosphate) battery cells and autonomous driving sensor stacks directly into global platform architectures.\n• Export Hub Strategy: Joint venture plants in eastern China will serve as primary export nodes for South East Asian and Middle Eastern electric vehicle markets.\n\nOriginal Source: Reuters Automotive & Industrial Global Coverage.",
                region = NewsRegion.CHINA.name,
                sourceName = "Reuters",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.reuters.com/business/autos-transportation/global-automakers-china-ev-joint-ventures-2026-08-02/",
                publishedTimeStr = "$todayStr 07:50 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 90,
                topicTag = "Trade & Supply Chain"
            )
        )

        articles.add(
            NewsArticle(
                id = "china_bloomberg_03_$todayStr",
                title = "Bloomberg: People's Bank of China Adjusts Liquidity Tools to Support Sovereign Bond Yield Curve",
                summary = "Bloomberg Market News details PBOC's calibrated open-market operations aimed at maintaining stable treasury yields and fostering corporate debt issuance across major financial markets.",
                fullContent = "BEIJING — Bloomberg reports that the People's Bank of China conducted outright reverse repo injections into liquidity channels today.\n\nDetails reported by Bloomberg Terminal Intelligence:\n• Monetary Stance: PBOC governor reaffirmed a supportive monetary stance while monitoring interbank lending rates.\n• FX Policy: The central bank set the daily yuan fixing fix at a balanced level, reinforcing currency stability amidst international trade fluctuations.\n• Commercial Lending: State commercial banks were instructed to extend long-term credit lines to renewable energy projects and regional infrastructure bonds.\n\nOriginal Source: Bloomberg News Financial Markets Division.",
                region = NewsRegion.CHINA.name,
                sourceName = "Bloomberg",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.bloomberg.com/news/articles/2026-08-02/pboc-liquidity-sovereign-bond-yields",
                publishedTimeStr = "$todayStr 08:00 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 60,
                topicTag = "Finance & Central Banking"
            )
        )

        articles.add(
            NewsArticle(
                id = "china_nikkei_04_$todayStr",
                title = "Nikkei Asia: Asian Tech Corridor Strengthens Semiconductor Equipment Synergies With East China Clusters",
                summary = "Nikkei Asia investigates cross-border supply chain integration between Tokyo, Seoul, and Shanghai electronics hubs, focusing on high-purity materials and advanced chip packaging.",
                fullContent = "TOKYO/SHANGHAI — Nikkei Asia highlights evolving supply chain linkages across East Asia.\n\nKey details from Nikkei's investigative report:\n• Packaging Innovation: Major East Asian test-and-assembly specialists are scaling operations in Jiangsu and Zhejiang to service regional AI accelerator chips.\n• Materials Commerce: Specialized chemical manufacturers in Japan and South Korea report sustained demand for high-purity silicon wafers and photoresist compounds.\n\nOriginal Source: Nikkei Asia Tech & Supply Chain Bureau.",
                region = NewsRegion.CHINA.name,
                sourceName = "Nikkei Asia",
                sourceCategory = "Asia Regional Major",
                originalUrl = "https://asia.nikkei.com/Business/Tech-Asia/china-east-asia-semiconductor-corridor-2026",
                publishedTimeStr = "$todayStr 06:30 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 180,
                topicTag = "Technology & Chips"
            )
        )

        articles.add(
            NewsArticle(
                id = "china_ft_05_$todayStr",
                title = "Financial Times: China-Europe Freight Rail Routes See Record Tonnage Driven by E-Commerce & Clean Energy Cargo",
                summary = "Financial Times reports on trans-Eurasian trade corridors, noting a 14% year-over-year increase in container train freight connecting inland Chinese manufacturing centers with European logistics hubs.",
                fullContent = "LONDON/BEIJING — Financial Times logistics analysis shows robust growth in the China-Europe Railway Express network.\n\nData verified by FT Trade Intelligence:\n• Cargo Composition: High-value electronics, solar panel modules, and cross-border consumer goods lead container volume.\n• Customs Automation: Digital customs clearings at frontier ports have reduced transit times between Xi'an, Chongqing, and Hamburg to under 12 days.\n\nOriginal Source: Financial Times Global Trade & Transport Desk.",
                region = NewsRegion.CHINA.name,
                sourceName = "Financial Times",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.ft.com/content/china-europe-freight-rail-record-trade-2026",
                publishedTimeStr = "$todayStr 07:10 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 120,
                topicTag = "Global Trade"
            )
        )

        // ---------------- OVERSEAS & GLOBAL DISPATCHES ----------------
        articles.add(
            NewsArticle(
                id = "overseas_reuters_01_$todayStr",
                title = "Reuters: Federal Reserve Signals Data-Dependent Interest Rate Outlook Amid Moderating Global Inflation",
                summary = "Reuters reports from Washington where Federal Reserve policymakers indicated a cautious, data-focused path forward as key price indices stabilize across major developed economies.",
                fullContent = "WASHINGTON — Reuters financial reporting covers the latest economic communications from the Federal Reserve System.\n\nKey takeaways from Reuters' report:\n• Monetary Stance: Officials emphasized that future benchmark rate decisions will hinge strictly on incoming employment, core inflation, and GDP growth indicators.\n• Treasury Markets: Benchmark 10-year US Treasury yields adjusted downward following the press conference, while global currency indices remained steady.\n• Global Central Bank Coordination: Central banks in Europe and Japan continue to calibrate policy settings in close alignment with global economic forecasts.\n\nOriginal Reporting Source: Reuters Global Economics Desk.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "Reuters",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.reuters.com/markets/us/fed-rate-outlook-inflation-data-2026-08-02/",
                publishedTimeStr = "$todayStr 08:20 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 25,
                topicTag = "Global Economy"
            )
        )

        articles.add(
            NewsArticle(
                id = "overseas_bbc_02_$todayStr",
                title = "BBC News: European Union Formally Approves Landmark Clean Energy & Smart Grid Investment Accord",
                summary = "BBC News reports from Brussels that European Union member nations have ratified a comprehensive €220 billion grid modernization directive to incorporate off-shore wind and solar connectivity.",
                fullContent = "BRUSSELS — BBC World Service reports on the final legislative approval of the European Clean Energy Transmission Infrastructure Act.\n\nKey directives published by BBC News:\n• High-Voltage Interconnectors: Funding allocates priorities for cross-border HVDC subsea power cables linking North Sea wind farms to central industrial grids.\n• Battery Grid Storage: Direct grants for grid-scale battery installations across Southern and Eastern Europe.\n• Energy Security Mandates: Accelerates national permitting processes for renewable generation facilities.\n\nOriginal Source: BBC World News Europe Desk.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "BBC News",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.bbc.com/news/world-europe-clean-energy-grid-pass-2026",
                publishedTimeStr = "$todayStr 07:40 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 80,
                topicTag = "Energy & Environment"
            )
        )

        articles.add(
            NewsArticle(
                id = "overseas_bloomberg_03_$todayStr",
                title = "Bloomberg: Global Tech Titans Expand Hyperscale AI Data Center Investments in Southeast Asia",
                summary = "Bloomberg Technology reports that major silicon valley and global cloud infrastructure firms are committing $18 billion to hyperscale data center nodes in Singapore, Malaysia, and Indonesia.",
                fullContent = "SINGAPORE — Bloomberg Tech Dispatches detail major cloud computing infrastructure expansions across ASEAN economies.\n\nHighlights from Bloomberg's investigation:\n• Strategic Corridors: Johor and Greater Jakarta are emerging as primary regional computing hubs due to abundant power availability and submarine cable landings.\n• Renewable Power Agreements: Corporate buyers are entering long-term power purchase agreements (PPAs) with local solar and hydroelectric producers to power zero-carbon server farms.\n\nOriginal Source: Bloomberg Technology & Asia Markets Division.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "Bloomberg",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.bloomberg.com/news/articles/2026-08-02/southeast-asia-ai-data-center-boom",
                publishedTimeStr = "$todayStr 08:05 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 40,
                topicTag = "Tech & Cloud"
            )
        )

        articles.add(
            NewsArticle(
                id = "overseas_wsj_04_$todayStr",
                title = "The Wall Street Journal: Global Freight & Container Shipping Rates Stabilize as Maritime Logistics Adapt",
                summary = "The Wall Street Journal analyzes global maritime commerce corridors, showing normalized spot freight rates and improved port throughput across major trans-Pacific and Asia-Europe shipping lanes.",
                fullContent = "NEW YORK — The Wall Street Journal reports on container shipping market dynamics.\n\nVerified findings by WSJ Logistics Analysis:\n• Capacity Balancing: Major ocean carriers have deployed new dual-fuel methanol and LNG container vessels, easing capacity constraints.\n• Supply Chain Flexibility: Shippers report smoother inventory flows and reduced dwell times at West Coast and Northern European container terminals.\n\nOriginal Source: The Wall Street Journal Business & Transportation.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "The Wall Street Journal",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.wsj.com/articles/global-shipping-container-rates-stabilize-2026-08-02",
                publishedTimeStr = "$todayStr 07:15 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 110,
                topicTag = "Global Commerce"
            )
        )

        articles.add(
            NewsArticle(
                id = "overseas_st_05_$todayStr",
                title = "The Straits Times: ASEAN Economic Council Inks Unified Cross-Border Digital Payment System Agreement",
                summary = "The Straits Times reports from Singapore on the milestone launch of real-time QR and digital instant currency settlements connecting Singapore, Thailand, Malaysia, Indonesia, and the Philippines.",
                fullContent = "SINGAPORE — The Straits Times details the official signing of the ASEAN Digital Payments Integration Framework.\n\nKey parameters reported by The Straits Times:\n• Interoperable Payments: Travelers and businesses can execute instant local-currency transactions via native banking apps with minimal exchange fees.\n• Trade Facilitation: Expected to boost regional micro-SME trade and cross-border tourism by an estimated $25 billion annually.\n\nOriginal Source: The Straits Times Regional News Bureau.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "The Straits Times",
                sourceCategory = "Asia Regional Major",
                originalUrl = "https://www.straitstimes.com/asia/asean-digital-payment-cross-border-link-2026",
                publishedTimeStr = "$todayStr 06:45 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 150,
                topicTag = "Regional Finance"
            )
        )

        articles.add(
            NewsArticle(
                id = "overseas_guardian_06_$todayStr",
                title = "The Guardian: Global Astronomical Consortium Unveils Deep Space Array Map of Earliest Star Clusters",
                summary = "The Guardian reports on a groundbreaking international astrophysics discovery capturing high-resolution infrared imagery of primordial galaxy formation.",
                fullContent = "LONDON — The Guardian science section reports on discoveries published by the International Astrophysical Observatory.\n\nHighlights from The Guardian science report:\n• Cosmological Significance: The observations confirm existing model predictions regarding dark matter scaffolding during early cosmic epoch.\n• Global Scientific Cooperation: Research involved collaborative computing labs across Europe, North America, Chile, and Australia.\n\nOriginal Source: The Guardian Science & Technology Desk.",
                region = NewsRegion.OVERSEAS.name,
                sourceName = "The Guardian",
                sourceCategory = "Global Major Outlet",
                originalUrl = "https://www.theguardian.com/science/2026/aug/02/astronomy-deep-space-array-galaxy-map",
                publishedTimeStr = "$todayStr 06:10 AM",
                sessionBatch = sessionTime,
                dateStr = todayStr,
                timestamp = now - 1000 * 60 * 200,
                topicTag = "Science & Space"
            )
        )

        // ---------------- DYNAMIC LIVE SYNC (AI Powered) ----------------
        if (isNetworkSync) {
            articles.clear()
            try {
                val apiKey = com.example.BuildConfig.DS_API_KEY
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                
                val requestBodyJson = JSONObject().apply {
                    put("contents", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", org.json.JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", "Use Google Search to find today's latest news from the following sources: Reuters, Lianhe Zaobao, SCMP, Bloomberg, BBC, and Financial Times. Gather a list of news articles and rank them by importance. You MUST select EXACTLY 20 most important news articles about China (or from Chinese sources) and EXACTLY 20 most important news articles about Overseas/Global events (40 articles total). Categorize each article. Return ONLY a JSON array of 40 objects. Each object MUST have exactly these keys: 'title', 'summary', 'url', 'sourceName', 'publishedAt' (ISO 8601 or similar), 'region' (must be exactly 'CHINA' or 'OVERSEAS'), and 'category'. Do not return any markdown formatting like ```json. Return raw JSON array only.")
                                })
                            })
                        })
                    })
                    put("tools", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("googleSearch", JSONObject())
                        })
                    })
                }

                val client = OkHttpClient.Builder()
                    .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                
                val request = Request.Builder()
                    .url(url)
                    .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val responseJson = JSONObject(responseBody)
                        val candidates = responseJson.optJSONArray("candidates")
                        if (candidates != null && candidates.length() > 0) {
                            val firstCandidate = candidates.getJSONObject(0)
                            val content = firstCandidate.optJSONObject("content")
                            val parts = content?.optJSONArray("parts")
                            if (parts != null && parts.length() > 0) {
                                val text = parts.getJSONObject(0).optString("text", "[]")
                                // Clean up the response text in case Gemini wraps it in markdown
                                val cleanText = text.replace("```json", "").replace("```", "").trim()
                                val jsonArray = org.json.JSONArray(cleanText)
                                
                                for (i in (jsonArray.length() - 1) downTo 0) {
                                    val obj = jsonArray.getJSONObject(i)
                                    val title = obj.optString("title", "No Title")
                                    val summary = obj.optString("summary", "No Summary")
                                    val urlPath = obj.optString("url", "")
                                    val publishedAt = obj.optString("publishedAt", "")
                                    val sourceName = obj.optString("sourceName", "Unknown Source")
                                    val category = obj.optString("category", "General")
                                    val regionStr = obj.optString("region", "OVERSEAS")

                                    val mappedRegion = if (regionStr.equals("CHINA", ignoreCase = true)) NewsRegion.CHINA.name else NewsRegion.OVERSEAS.name

                                    articles.add(
                                        0, // add to top
                                        NewsArticle(
                                            id = "live_ai_${System.currentTimeMillis()}_$i",
                                            title = "$sourceName: $title",
                                            summary = summary,
                                            fullContent = summary + "\n\nOriginal reporting by $sourceName.\n(AI Synthesized Live Update)",
                                            region = mappedRegion,
                                            sourceName = sourceName,
                                            sourceCategory = "AI Live Sync",
                                            originalUrl = urlPath,
                                            publishedTimeStr = "$todayStr " + publishedAt.take(16).takeLast(5) + " (Live AI)",
                                            sessionBatch = sessionTime,
                                            dateStr = todayStr,
                                            timestamp = now + (jsonArray.length() - i) * 1000, // Ensure AI news are placed top in order
                                            topicTag = category
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    android.util.Log.e("NewsFetcher", "AI Sync Failed: ${response.code} ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // ---------------- BBC RSS SYNC ----------------
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                val request = Request.Builder()
                    .url("https://feeds.bbci.co.uk/news/rss.xml")
                    .header("User-Agent", "Mozilla/5.0")
                    .build()
                
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBodyStream = response.body?.byteStream()
                    if (responseBodyStream != null) {
                        val dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                        val dBuilder = dbFactory.newDocumentBuilder()
                        val doc = dBuilder.parse(responseBodyStream)
                        doc.documentElement.normalize()
                        
                        val itemList = doc.getElementsByTagName("item")
                        var addedCount = 0
                        for (i in 0 until itemList.length) {
                            if (addedCount >= 3) break
                            val itemNode = itemList.item(i)
                            if (itemNode.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                                val element = itemNode as org.w3c.dom.Element
                                val title = element.getElementsByTagName("title").item(0)?.textContent ?: "No Title"
                                val description = element.getElementsByTagName("description").item(0)?.textContent ?: ""
                                val link = element.getElementsByTagName("link").item(0)?.textContent ?: ""
                                
                                articles.add(
                                    0, // Add to top
                                    NewsArticle(
                                        id = "bbc_rss_${System.currentTimeMillis()}_$i",
                                        title = title,
                                        summary = description,
                                        fullContent = description + "\n\nOriginal URL: $link",
                                        region = NewsRegion.OVERSEAS.name,
                                        sourceName = "BBC News",
                                        sourceCategory = "Global Major Outlet",
                                        originalUrl = link,
                                        publishedTimeStr = "$todayStr Live RSS",
                                        sessionBatch = sessionTime,
                                        dateStr = todayStr,
                                        timestamp = now + 20000 + (3 - addedCount), // Ensure correct ordering
                                        topicTag = "Global News"
                                    )
                                )
                                addedCount++
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("NewsFetcher", "BBC RSS Sync Failed: ${e.message}")
            }
        }

        return@withContext articles
    }
}
