sed -i '77,86c\
        // Initial setup and auto-refresh on app open\
        viewModelScope.launch {\
            refreshDigest(showToast = false)\
        }' app/src/main/java/com/example/ui/NewsViewModel.kt
