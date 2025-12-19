package core;

public enum StatusCode {

    //enum is a special "class" that represents a group of constants
    // (unchangeable variables, like final variables)
    //For notes: https://chat.deepseek.com/a/chat/s/163d7115-cc08-4ae9-8f21-5855bddc84e1

//    Enum = a special class with fixed values.
//    Each value (like MONDAY or SUCCESS) = an object of that enum type.
//    You can optionally give each value its own data (like a code, message, or description).

    //consider each one of the below values as Objects.

    SUCCESS(200, "The request succeeded"),
    // the above line means --> StatusCode SUCCESS = new StatusCode(200, "The request succeeded")
    // but since its enum its shorter and built-in i.e. enum works like this
    // and since we're passing these 2 values, the constructor initializes code and message with these 2 values
    // for the SUCCESS Constant

    CREATED(201, "A new resource was created"),

    BAD_REQUEST(400, "Missing required field"),

    UNAUTHORIZED(401, "Invalid access token"),

    NOT_FOUND(404, "Cannot find requested resource"),

    NO_CONTENT(204, "No content to send in the response body") ;


    public final int code;
    public final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}


// Enums are Special Classes with Fixed Objects
// In Java, an enum is a special type of class that represents a fixed set of constants (objects).
//- SUCCESS, CREATED, NO_CONTENT, etc., are enum constants (objects) of the StatusCode enum.
//- Each of these is an object of the StatusCode class.
//- Unlike regular classes, you cannot create new instances of an enum using new.


// How code and message Work
//- code and message are instance variables (just like in a regular class).
//   - The constructor StatusCode(int code) is called automatically when the enum is loaded.
//      Example: SUCCESS(200) → Calls StatusCode(200) and sets this.code = 200.
//   - code is a public field, so you can access it directly (e.g., StatusCode.SUCCESS.code).


//- When you declare an enum constant like SUCCESS(200, "The request succeeded"), you are:
//---- Creating an object of StatusCode
//---- Calling the constructor StatusCode(int code, String message)
//---- Setting this.code = 200 and this.message = "The request succeeded" for that object.