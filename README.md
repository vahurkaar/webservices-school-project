# webservices-school-project

This project was part of my school assignment in Tallinn University of Technology.
The course was on webservices and their design (SOAP and REST). The assignment was to create a system of 2 web services about videogames, where one web services uses the other.

This project consists of 2 SOAP webservice applications (inner and outer). Both have been implemented with Spring Web Services and JAXB. 

The idea behind these webservices is that one web service (inner) only serves static data about videogames, it acts as a database. The other webservice (outer) acts as an intelligent service, which takes specific filter parameters and calculates the list of videogames, which the gamer is able to play.

The assignment was completed in November 2013. This project demonstrates my knowledge on how to implement SOAP web services with Spring Framework.
