Shuffle - Locust notes

- Failures started to manifest around 2039 users, but it really started to pick up, before kind of stabilizing around 27% failures, and then reaching 50% failure around 6890 users
- Before I stopped the process around 50% failures, I got a warning in my terminal about CPU usage
    - At this point, I had reached 2712.6 RPS
- For the long function, I’m not sure exactly when the “Too many open files” failure arose, but there were 4576 fails of that type. There were also 5536 “ConnectionResetError” fails, 1595 “Broken pipe” errors, 403 “Can’t assign requested address” errors, and 766 “gaierror” fails.
- For the short function, I’m not sure exactly when the “Too many open files” failure arose, but there were 10574 fails of that type. There were also 930 “Can’t assign requested address” fails, and 1739 “gaierror” fails.
- Java was personally, against popular opinion, easier to write because I spent a lot of my summer working with Java, so I was used to it. Plus dealing with the input and output using Scanner was more familiar to me (due to Dorin’s 186 class)than the Python input/output was.
    - However, Java seems to have a very negligible improvement in terms of the number of users, and seemed to be worse in regards to RPS, than Python did
    - For this service though, while the Java program had failures manifesting quicker than they did in Python, it stabilized at some point, before picking back up. The Python one never stabilized. It just rose in failures.
