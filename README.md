# LAPS
SA4105 CA project: Leave Application  Processing System


How to run ReactJS
Steps
1. Install Node.js and npm
    Download and install Node.js and npm here.  https://nodejs.org/
    Verify the installation:

2. Set up npm folder and path
    Create an npm folder at C:\Users\<Your Username>\AppData\Roaming (if it doesn't exist).  //you should use you own username
    Add the npm path to the system environment variable PATH:
    Open System Properties -> Advanced system settings -> Environment Variables
    Find and edit the Path variable in the System variables section
    Add the new path: C:\Users\<Your Username>\AppData\Roaming\npm

3. Run Spring Boot

4. In a new terminal ,Set up the frontend environment
    Navigate to the frontend project directory:
        cd LAPS-new/laps-client
    Install project dependencies:
        npm install      
    Run the frontend project:
        npm start     //IMPORTANT !!! Under LAPS-new/laps-client

5.You enter http://localhost:3000/login in your broswer,you will see a login page.

6.you can enter correct or wrong username and password,
  if correct, page will alert Login successful, you can check 
  http://localhost:8080/,it will not be the login page ,instead,it is http://localhost:8080/home.
  if wrong,page will alert Login failed,nothing will happen to http://localhost:8080/.
  