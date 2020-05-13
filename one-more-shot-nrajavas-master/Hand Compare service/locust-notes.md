Compare - Locust notes

- Failures started to manifest fairly quickly around 900 users, and it started to pick up, before kind of stabilizing around 28% failures, and then reaching 60% failure around 8000 users
- Before I stopped the process at 60% failures, I got a warning in my terminal about CPU usage
    - At this point, I had reached 3157.6 RPS
- For the long function, I’m not sure exactly when the “Too many open files” failure arose, but there were 7160 fails of that type. There were also 5338 “Connection reset by peer” errors, 1840 “Broken pipe” errors, 969 “gaierror” fails, and 583 “Can’t assign requested address” errors.
- For the short function, I’m not sure exactly when the “Too many open files” failure arose, but there were 16743 fails of that type. There were also 1329 “Can’t assign requested address” fails, and 2386 “gaierror” fails.
- Java was personally, against popular opinion, easier to write because I spent a lot of my summer working with Java, so I was used to it. Plus dealing with the input and output using Scanner was more familiar to me (due to Dorin’s 186 class)than the Python input/output was.
    - This service seems to have done a little better when compared to its Python equivalent, as opposed to the Shuffle service, which was as good, if not slightly worse, than the same service in Python.
    - For this service, while the Java program had failures manifesting quicker than they did in Python, it stabilized at some point, before picking back up. The Python one never stabilized. It just rose in failures.
