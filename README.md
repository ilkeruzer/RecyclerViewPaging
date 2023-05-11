## RecyclerView Paging
Paging data display with RecyclerView onScrolled.

## App Using 🙌
- Random data is drawn from the DataSource.kt object when the application is first opened. If there is no data, we print the error on the screen with Toast. The user is expected to trigger the SwipeRefreshLayout again.
- As the scroll moves down, the next data is loaded onto the screen. In case of an error, a message is displayed on the screen. You need to trigger the scroll down again. (The reason we trigger the scroll again is not to tire the backend service.)
- After taking all the data, the message "There is no one left to list." is written. To see more data, you need to scroll to the first data and trigger SwipeRefreshLayout. Thus, the application flow starts again.

## Screenshots 📷
<img src="/arts/Screenshot_20230511_143824.png" width="260"> &emsp;<img src="/arts/Screenshot_20230511_143857.png" width="260"> &emsp;<img src="/arts/Screenshot_20230511_143913.png" width="260">

## More 👏
- If you want to review my other projects, you can check my profile.