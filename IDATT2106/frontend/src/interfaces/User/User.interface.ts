export default interface User {
  userId: number;
  forename: string;
  surname: string;
  email: string;
  score: number;
  rating: number;
  role: string;
  dateOfBirth?: string;
  profilePicture?: string;
  trainingLevel: string;
  trusted: boolean;
}
