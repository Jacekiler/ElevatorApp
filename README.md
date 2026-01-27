 # Frontend
After running docker composed services, frontend is served on localhost:4200 (as set in docker-compose.yaml/.env).
Backend API used in frontend is set as env var in ele_front.env and pass to frontend on runtime.
Frontend allows to operate on elevators simulator, both as a user inside the elevator as well as a user outside the elevator.
It shows current floor, nearest target floor and there is a simple open/close door animation.
 
# Assumptions and decisions
- Multiple elevators in a building, separated one from each other. 
- Number of elevators, minimum and maximum floor can be configured in application.yaml.
- Also number of threads working on elevators operations can be set.
- Each elevator has its own engine which manages the elevator's operations.
- Engines are run in periodical cycles by a scheduler.
- API: User can call an elevator from the outside pointing UP or DOWN direction.
- API: User can select a floor from inside the elevator.
- API: User can open the doors while elevator state permits that.
- API: User can close the doors while elevator state permits that.
- API: User can receive states of all elevators.
- API: User can receive a state of specified elevator by id
- Elevator state is important only in the lifecyle of the system, so there is no need for database in my opinion.
- Elevator.upRequest, Elevator.downRequests - direction from elevator perspective


# To run application (backend + frontend)
1. mvnw clean package
2. docker compose up

