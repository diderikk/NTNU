export default interface MakeActivity {
  title: string;
  description: string;
  equipment: string;
  difficulty: number;
  city: string;
  place: string;
  longitude: number;
  latitude: number;
  startTime: string;
  durationMinutes: number;
  maxParticipants: number;
  type: string;
  privateActivity: boolean;
  activityPicture?: string;
}
