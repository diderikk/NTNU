import torch
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import pandas as pd


day_length_weight = pd.read_csv("./day_length_weight.csv")
day_length_weight_tensor = torch.tensor(day_length_weight.values, dtype=torch.float)

x_train = day_length_weight_tensor[:,1:3].reshape(-1, 2)
y_train = day_length_weight_tensor[:, 0].reshape(-1,1)


class LinearRegressionModel:
    def __init__(self):
        # Model variables
        self.W = torch.randn((2,1), requires_grad=True) #[[a1, a2]] 
        self.b = torch.randn((1,1), requires_grad=True)

    # Predictor
    def f(self, x):
        return x @ self.W + self.b  # @ corresponds to matrix multiplication
    
    def loss(self, x, y):
        return torch.nn.functional.mse_loss(self.f(x), y) 

model = LinearRegressionModel()
print(model.W)
optimizer = torch.optim.SGD([model.W, model.b], lr=0.00000075)

for epoch in range(10000):
	loss = model.loss(x_train, y_train).backward()
	optimizer.step()

	# print(f'epoch = {epoch}, loss= {model.loss(x_train, y_train)}, W = {model.W}, b = {model.b}')

	optimizer.zero_grad()

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

x = torch.linspace(int(torch.min(x_train[1:, 1:]).item()), int(torch.max(x_train[1:, 1:]).item())+200, 1000)
z = torch.linspace(int(torch.max(x_train[1:, 0:1]).item()), int(torch.max(x_train[1:, 0:1]).item())+200, 1000)
Y = model.f(x_train).detach()

ax.scatter(x,Y,z, label='Predicted values')
ax.scatter(x,y_train, z, label='Correct values')

ax.set_xlabel('length')
ax.set_ylabel('weight')
ax.set_zlabel('days')

plt.legend()
plt.show()