# Android App Testing Automation

This repo contains a simple Android code with following screens, functionalities.
1. Splash screen
2. Home screen (Loading data from API) - Listing data 
3. Details screen (Details view) - Image loading and description about img

Create automated tests for App flow & data validations , some of them are:
1. Open app (Splash screen should come when fresh open.), and check splash screen timing (should be 5 secs)
2. Splash screen should load home screen
3. Once data listed (Check size of list data from API), automated click on any item should launch Details screen
4. Check title of screen is changed to 'Details'
5. Image should load with detail description available on details screen
6. Press back (Back to Home list - automated click)
7. Scroll down & click on last item of list

# Bonus Points
* In case of no internet, App flow handling 
* Log of success & failure case { Crashes & invalid data report }
* Automated test of flow like Loading dialogs, dialog contents, dialog actions 

# Test Automation Tool
Any appropriate tool can be used to write automated test cases. e.g. Android Esspreso, Appium, MonekyRunnr

# Reply
You are expected to Fork this repo, write your test cases, push changes to your repo, and email us a link of it to jobs@sportscafe.in

Let us know the tool you used when reply

Feel free to reach out us for any clarification
