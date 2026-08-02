sed -i '138,153c\
                Row(\
                    verticalAlignment = Alignment.CenterVertically\
                ) {\
                    Icon(\
                        imageVector = Icons.Default.Update,\
                        contentDescription = "Last Refresh",\
                        tint = Color.White.copy(alpha = 0.7f),\
                        modifier = Modifier.size(15.dp)\
                    )\
                    Spacer(modifier = Modifier.width(4.dp))\
                    Text(\
                        text = "Last sync: ${scheduleStatus.lastRefreshedTimeStr}",\
                        style = MaterialTheme.typography.bodySmall,\
                        color = Color.White.copy(alpha = 0.85f)\
                    )\
                }' app/src/main/java/com/example/ui/ScheduleHeader.kt
sed -i '155,159d' app/src/main/java/com/example/ui/ScheduleHeader.kt
