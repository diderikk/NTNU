const getActivityDifficultyNames = (difficulty: number) => {
  switch (difficulty) {
    case 7:
      return "Lett, Middels, Krevende";
    case 6:
      return "Middels, Krevende";
    case 5:
      return "Lett, Krevende";
    case 4:
      return "Krevende";
    case 3:
      return "Lett, Middels";
    case 2:
      return "Middels";
    case 1:
      return "Lett";
    default:
      return "";
  }
};

export default getActivityDifficultyNames;
