package org.example;

public class CreatingCourier {
        // создаем поля для создания курьера
        private String login;
        private String password;
        private String firstName;
        private String id;

        public CreatingCourier(String login, String password, String firstName) {
            this.login = login;
            this.password = password;
            this.firstName = firstName;
        }

        public CreatingCourier(){

        }
        // Создаем геттер и сеттер для login
        public void setLogin(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }
        // Создаем геттер и сеттер для Password
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        // Создаем геттер и сеттер для FirstName
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

