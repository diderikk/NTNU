let string = '5 / 3';
let arr = [1, "ANS"];

arr.filter(item => item === 'ANS').forEach(function(item, index) {
    this[index] = 12;
}, arr);

console.log(arr);



