# Booking

This project is an accumulation of my learnings related to Domain Driven Design. It's still a work in progress repository, but you can see how the contexts are organized within code. 
The problem this code is trying to solve is Booking of hotel rooms.
Project is organized as a modular monolith, allowing for further breakdown into independent services when the need arrives.

## Contexts
You can see following bounded contexts distilled
* Availability - context defining whether an item is available
* Details - a CRUD context for storing reservation details like customer name, number of guests etc
* Pricing - context answering the question of "how much it costs?"

