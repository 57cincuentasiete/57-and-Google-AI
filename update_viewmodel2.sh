sed -i '71,80c\
        // Initial setup and auto-refresh on app open\
        viewModelScope.launch {\
            repository.ensureInitialData()\
            updateScheduleStatus()\
            refreshDigest(showToast = false)\
        }' app/src/main/java/com/example/ui/NewsViewModel.kt
