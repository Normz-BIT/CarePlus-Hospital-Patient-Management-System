# CarePlus Hospital Patient Management System

GUI Patient Management System with a TCP/IP socket-based Client/Server architecture.

CIT3009 Advanced Programming, University of Technology, Jamaica.

Patients can register complaints, book appointments, view their medical and payment
records, and chat with staff. Receptionists triage and assign complaints, doctors record
diagnoses and schedule follow-ups, and nurses record vital signs. The client talks to the
server over a socket, and only the server touches the database.

---

## What you need before you start

- **JDK 21 or newer**
	- The project is compiled at source and target level 21.
	- Check with `java -version` and `javac -version`.
- **MySQL 8** or **MariaDB 10.4+**
	- XAMPP ships MariaDB and works fine.
	- The server must be running and reachable on `localhost:3306`.
- **Maven 3.8+**
	- Only needed if you build from the command line. Eclipse has Maven built in.
- **Eclipse IDE** (recommended, the project is set up for it)
	- Any IDE works, but the steps below assume Eclipse.
- **Git**

---

## Setup

### 1. Clone the repository

	git clone https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	cd CarePlus-Hospital-Patient-Management-System

### 2. Import the project into Eclipse

- File > Import > Maven > Existing Maven Projects
- Browse to the cloned folder and select the `careplus_pms` directory
- Eclipse should detect four projects. Make sure all are ticked:
	- `careplus_pms` (the parent)
	- `careplus_common`
	- `careplus_server`
	- `careplus_client`
- Click Finish and wait for Maven to download the dependencies. This takes a few minutes
  the first time.

If you would rather build from the command line:

	cd careplus_pms
	mvn clean install

### 3. Create the database user

The application connects to MySQL as its own user. Create it once, from the MySQL command
line or phpMyAdmin:

	CREATE USER 'carePlusAdmin'@'localhost' IDENTIFIED BY 'choose_a_password';
	GRANT ALL PRIVILEGES ON *.* TO 'carePlusAdmin'@'localhost';
	FLUSH PRIVILEGES;

- The privileges have to be server-wide (`*.*`), not just on one database. The server
  creates and drops the `careplus_db` database itself, so it needs `CREATE DATABASE` and
  `DROP DATABASE` as well as the usual table and row permissions.
- You can use a different username. If you do, use it in the next step too.

### 4. Add your database credentials

The credentials file is deliberately not in the repository, so a fresh clone will not have
one. You have to create it yourself:

- Go to `careplus_pms/careplus_server/src/main/resources/`
- Copy `hibernate.properties.template` and rename the copy to `hibernate.properties`
- Open it and fill in the user you just created:

	hibernate.connection.username=carePlusAdmin
	hibernate.connection.password=choose_a_password

Notes:

- `hibernate.properties` is listed in `.gitignore`, so your password will not be committed.
  Leave it that way.
- The rest of the connection settings (the URL, the driver, the dialect) are already in
  `hibernate.cfg.xml` in the same folder. You only need to touch that file if your MySQL
  is not on `localhost:3306`.

### 5. Start the server

- Open `careplus_pms/careplus_server/src/main/java/com/careplus/server/controller/ServerController.java`
- Run it as a Java Application

The server window opens and does the following on its own:

- Checks whether the `careplus_db` database exists
- If it does not, creates it by running `careplus_create_database.sql`, which builds every
  table and loads the sample data
- Connects Hibernate to it

You should see this in the server console:

	Database connection established.

If instead it says the connection failed, see Troubleshooting below.

Then click **Start Server**. The console shows:

	Server started on port 8888.

Leave this window open. The client cannot do anything without it.

### 6. Start the client

- Open `careplus_pms/careplus_client/src/main/java/com/careplus/client/launcher/ClientApp.java`
- Run it as a Java Application

The login window opens.

There is only one client application. Whether you get the patient dashboard or the staff
dashboard depends on who logs in, so you do not pick beforehand.

### 7. Log in

Use any of the accounts loaded by the sample data.

Patients (password `pat123`):

| ID      | Name             |
|---------|------------------|
| PAT0001 | Andre Campbell   |
| PAT0002 | Shanice Brown    |
| PAT0003 | Marcus Thompson  |
| PAT0004 | Taneisha Walker  |

Staff (password `staff123`):

| ID      | Name             | Role         |
|---------|------------------|--------------|
| STF0001 | Karen Reid       | Doctor       |
| STF0002 | David Henry      | Doctor       |
| STF0003 | Paula Grant      | Nurse        |
| STF0004 | Michael Forbes   | Nurse        |
| STF0005 | Janet Williams   | Receptionist |

The ID is not case sensitive, so `pat0001` works as well as `PAT0001`. The password is.

Once you are in, the menu bar only shows the features your role is allowed to use:

| Role         | Menu items                                                    |
|--------------|---------------------------------------------------------------|
| Patient      | Appointments, Complaints, Medical Records, Payments, Chat      |
| Doctor       | Diagnosis, My Patients, Doctors, Staff Chat                    |
| Nurse        | Nurse Station, Staff Chat                                      |
| Receptionist | Complaint Manager, Staff Assignment, Doctors, Staff Chat       |

To try the full workflow, run a second client at the same time and log in as a member of
staff, so you can watch a complaint move from the patient to the receptionist and back.

---

## Running more than one client

The server handles several clients at once, each on its own thread. To demonstrate this,
just run `ClientApp` again. In Eclipse, use Run > Run Configurations and launch the same
configuration a second time.

---

## Resetting the database

The server window has a **Clear/ Reset Database** button. It drops `careplus_db` and rebuilds
it from the script, which puts the sample data back exactly as it started.

- This deletes everything currently in the database. There is no undo.
- Use it if the data gets into a mess during testing, or before a demo so you start clean.
- The server stops and restarts itself around the reset, so you do not need to close
  anything.

---

## Troubleshooting

**"Database connection failed - check hibernate.properties"**

- Is MySQL actually running? In XAMPP, check the MySQL row says Running.
- Did you create `hibernate.properties`? A fresh clone does not include one, see step 4.
- Are the username and password in it correct?
- Does that user have server-wide privileges? See step 3.

**"Could not reach the MySQL server"**

- MySQL is not running, or it is not on `localhost:3306`.
- If it is on another port, change the URL in
  `careplus_server/src/main/resources/hibernate.cfg.xml`.

**The client opens but nothing loads, or every screen is empty**

- The server is probably not started. Check the server window says
  `Server started on port 8888.`
- Starting the server window alone is not enough, you also have to click Start Server.

**"Unable to listen on port 8888"**

- Something else is already using that port, most likely a second copy of the server.
  Close the other one.

**Login always fails**

- Check the password. `pat123` for patients, `staff123` for staff.
- If the accounts do not seem to exist at all, the sample data may not have loaded. Use
  Clear/Reset Database on the server window.

**Chat says it is outside operating hours**

- That is intentional. Live chat only accepts messages between 8:00 a.m. and 7:00 p.m.,
  as required by the brief. Reading old messages still works outside those hours.
- The hours are constants at the top of
  `careplus_server/src/main/java/com/careplus/server/service/ChatService.java`
  if you need to widen them for a late demo.

---

## Where things are

	careplus_pms/
		careplus_common/     shared code: models, enums, the request/response protocol,
		                     the login screen and the MDI dashboard shell
		careplus_server/     socket server, services, Hibernate setup, the SQL script
		careplus_client/     the patient and employee screens and their controllers

Logs are written to `logs/rolling.log` in the working directory, and to the console.
