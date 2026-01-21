 **Disclaimer:** there is no frontend. I'm not that familiar to do it quick and I don't want to generate it just to have it. I need to trust in my backend and believe that time is important, so I don't delay the task for next days :)
 
# Assumptions and decisions
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
- 


# To run application
1. mvnw clean package
2. docker compose up

