# OpenWeather API Project

This project is a Java-based application that tests the OpenWeatherMap API for retrieving weather data. It includes automated tests to ensure the API responses meet the expected schema and values.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Internet connection to access the OpenWeatherMap API

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/openWeather.git
   cd openWeather
   ```

2. Configure the API key:
   - Open `src/test/java/com/weatherapi/tests/TestConfig.java`.
   - Replace the `API_KEY` value with your OpenWeatherMap API key.

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

## Running the Tests

To execute the tests, run the following command:

```bash
mvn test
```

This will run all the test cases defined in the project, validating the API responses against the defined schema and expected values.

## License

This project is licensed under the MIT License.