# Booking

This project is an accumulation of my learnings related to Domain Driven Design. It's still a work in progress repository, but you can see how the contexts are organized within code. 
The problem this code is trying to solve is Booking of hotel rooms.
Project is organized as a modular monolith, allowing for further breakdown into independent services when the need arrives.

## Contexts
You can see following bounded contexts distilled
* Owners - a simple CRUD solution for adding/updating owner data
* Items - a CRUD solution for adding/removing hotel rooms we'd later want to book
* Bookings - a solution with a slightly more complex domain allowing for booking hotel rooms, following a hexagonal architecture approach
* Notifications - a solution for sending notifications, reacting on various events.
