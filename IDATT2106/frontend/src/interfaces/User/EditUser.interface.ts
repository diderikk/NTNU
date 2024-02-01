//Cannot inherit another User interface because password and old password needs to be optional
export default interface EditUser {
  userId: number;
  forename: string;
  surname: string;
  email: string;
  newPassword?: string;
  oldPassword?: string;
  profilePicture?: string;
  trainingLevel: string;
}
