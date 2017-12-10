
## Yelp Search Application
----

In this project a target application which runs queries on the Yelp data and extracts useful information will be developed. The primary users for this app will be potential customers seeking for business that match their search criteria. This app has a user interface that supports business search based on categories (main categories and sub-categories) and the attributes associated with each business category. Different business characteristics such as main category(ies), sub-category(ies), business attributes, days of the week, hours of a day, city and state will be utilized as the search criteria. The application should also all the user no not only view the business that match the selected criteria, but also view reviews provided for each business.

*Faceted search* has become a popular technique in commercial search applications, particularly for online retailers and libraries. It is a technique for accessing information organized according to a faceted classfication system, allowing users to explore a collection of information by applying multiple filters. Faceted search is the dynamic clustering of items or search results into catefories that let users drill into search results (or even skip searching entirely) by any value in any field. Users can then "drill down" by applying specific constraints to the search results. Look at "https://react.rocks/tag/Faceted_Search" for some examples.

In this application, the user can filter the search results using available business attributes (i.e. facets) such as main category(ies), sub-categpry(ies), business attributes, days of the week, hours of a day, city and state. Each time the user clicks on a facet value; the set of results is reduced to only the items that have that value. Additional clicks continue to narrow down the search-- the previous facet values are remembered and applied again.

## Project Details
### 0. Part 0
Install Oracle Database 11gR2 or later. If you are using a MAC laptop, you can install a virtualization software such as Virtual Box, and install a Windows or Linux guest operation system. Then install Oracle Database on this environment. Also you can install and setup docker.

### 1. Part 1
1. Download the Yelp dataset. There are four json resource files, namely yelp_business.json, yelp_checkin.json, yelp_review.json, yelp_user.json.
2. Design database schema for the described application to query the data from databasein an efficient way.
3. Produce DDL SQL statements for creating the corresponding tables in a relational DBMS.
4. Populate the database with the dataset. Generate INSERT statements for the tables and run those to insert data into DB.
5. After the database if populated, create indexes on frequently accessed columns of the tables using CREATE INDEX statement.

### 2. Part 2
1. Business Search: Implement a GUI where the user can search for business that match the criteria given;
    - Once the appplication is loaded, main categories values are loaded from backend database;
    - The user is required to select at lease on main categories value. For example, *Restaurants* is chosen;
    - The subcatogories matching the previous main categories selection will be listed under Business Subcategory panel. Since user selected *Restaurants* in previous step, only subcategories that its main category is *Restaurants* should appear in the subcategory panel. This attribute is optional and user might not select a subcategory. Assume user selects *Mediterranean* as subcategory value.
    - Business attributes are the next selection. This attribute is also optional in building the query. Since user selects Restaurants and Mediterranean, only attribute values appeared in business with maincategory = *Restaurants* **AND** subcategory = *Mediterranean* should appear in the attribute selection panel. Assume that user selects *Outdoor Sitting* as the desired attribute.
    - The specific state and city of the business corresponding to the previous selections will appear in "Location" drop down menu. This attribute is also optional in building the query. Since user selected *Restaurants*, and *Mediterranean* and *Outdoor Sitting* in previous steps, only location (city, state) values of business with maincategory = *Restaurants* **AND** subcategory = *Mediterranean* **AND** thos that provide Outdoor Sittings should appear in the location dropdown menu. Assume *Phoenix, AZ* as the selected location.
    - The operation days of the business corresponding to the previous selections will be appeared in "Days of week" drop down menu. Also the operation time of the business corresponding to the previous selections will be appeared in the From/To dropdown menus. These attributes are also optional in building the query. Based on previous selections, operation days and times corresponding to business with maincategory = *Restaurants* **AND** subcategory = *Mediterranean* **AND** those that provide *Outdoor Sittings* should appear in days of week and from/to menus.
2. Review Search: Select a certain business in the search results and list all the reviews for the business.