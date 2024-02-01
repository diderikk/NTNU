export default interface IActivity {
  activityId: number;
  city: string;
  description: string;
  difficulty: number;
  durationMinutes: number;
  equipment: string;
  latitude: number;
  longitude: number;
  organizerId: number;
  place: string;
  privateActivity: boolean;
  startTime: string;
  title: string;
  maxParticipants: number;
  activityPicture: string;
  organizerForename: string;
  organizerSurname: string; 
  chatId: number;
}
