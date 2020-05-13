Compare - Locust notes

- Failures started to manifest around 2129 users (1%), but it really started to pick up steadily, reaching 50% failure around 6000 users
- Before I stopped the process at 51% failures, I got a warning in my terminal about CPU usage
    - At this point, I had reached 2673.4 RPS
- For the long function, I’m not sure exactly when the “Too many open files” failure arose, but there were 10869 fails of that type. There were also 489 “ConnectionResetError” fails,  109 “Broken Pipe” errors, and 3781 “gaierror” fails
- For the short function, I’m not sure exactly when the “Too many open files” failure arose, but there were 25350 fails of that type. There were also 1142 “ConnectionResetError” fails,  182 “Broken Pipe” errors, and 8586 “gaierror” fails
