import LogInUser from "./LoginUser.interface";

export default interface SignUpUser extends LogInUser {
  forename: string;
  surname: string;
  email: string;
  dateOfBirth?: string;
  profilePicture?: string;
  //Has to be string to be sendt over JSON
  trainingLevel: string;
}
