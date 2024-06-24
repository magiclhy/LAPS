# LAPS Team 2
SA4105 CA project: Leave Application Processing System

Follow the following instructions to run the application locally:

## Backend Setup
1. Open "MySQL Workbench", create a new schema, name the schema as "leaveapp". (Double quotes not included)
2. Set "leaveapp" schema as default schema.
3. Open "Database" folder, run "leaveapp.sql" on MySQL as a script.
4. Import "Code" folder into Java Spring Boot.
5. Open "src/main/resources/application.properties", modify the username and password to match yours for MySQL.
6. Run "src/main/java/sg.nus.iss.java/Application.java".
7. Type in "localhost:8080" into your browser search box.

## Frontend Setup

1. Install Node.js and npm
Download and install Node.js and npm from [here](https://nodejs.org/).
Verify the installation:
node -v
npm -v

2. Set up npm folder and path
Create an npm folder at C:\Users\<Your Username>\AppData\Roaming (if it doesn't exist).
Add the npm path to the system environment variable PATH:
Open System Properties -> Advanced system settings -> Environment Variables
Find and edit the Path variable in the System variables section
Add the new path: C:\Users\<Your Username>\AppData\Roaming\npm

3. Run Spring Boot
Ensure the backend is running as described in the Backend Setup.

4. Set up the frontend environment
In a new terminal, navigate to the frontend project directory:
cd laps-client
Install project dependencies:
npm install
Run the frontend project:
npm start

5. Access the login page
Open your browser and navigate to:http://localhost:3000/login
You will see the login page.

6. Test login functionality
You can enter correct or incorrect username and password:

If the credentials are correct, the page will alert "Login successful". You can then check http://localhost:8080/, and it will redirect to http://localhost:8080/home instead of the login page.
If the credentials are incorrect, the page will alert "Login failed", and nothing will change on http://localhost:8080/.

## User Credentials
Usernames:
    Employee account: employee1
    Manager account: manager1
    CEO account: ceo
    Admin account: admin1
Password for all accounts is the same as the username.