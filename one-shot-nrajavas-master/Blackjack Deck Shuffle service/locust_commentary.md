Shuffle - Locust notes

- Failures started to manifest around 2225 users (5%), but it really started to pick up quickly, reaching 50% failure around 6500 users
- Before I stopped the process at 54% failures, I got a warning in my terminal about CPU usage
    - At this point, I had reached 3071.5 RPS
- For the long function, I’m not sure exactly when the “Too many open files” failure arose, but there were 16278 fails of that type. There were also 272 “ConnectionResetError” fails,  and 4884 “gaierror” fails
- For the short function, I’m not sure exactly when the “Too many open files” failure arose, but there were 37825 fails of that type. There were also 618 “ConnectionResetError” fails,  and 11379 “gaierror” fails
