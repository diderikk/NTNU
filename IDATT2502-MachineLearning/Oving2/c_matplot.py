import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import torch

x_train = torch.tensor([[0,0], [0,1], [1,0], [1,1]], dtype=torch.float)
y_train = torch.tensor([[0], [1], [1], [0]], dtype=torch.float)

class XorOpertorNN:
	def __init__(self):
		self.W1 = torch.randn((2,2), requires_grad=True, dtype=torch.float)
		self.b1 = torch.randn((1,2), requires_grad=True, dtype=torch.float)
		self.W2 = torch.randn((2,1), requires_grad=True, dtype=torch.float)
		self.b2 = torch.randn((1,1), requires_grad=True, dtype=torch.float)
	
	def parameters(self):
		return [self.W1, self.b1, self.W2, self.b2]

	def f1(self, x):
		return torch.sigmoid(x @ self.W1 + self.b1)
	
	def f2(self, x):
		return torch.sigmoid(x @ self.W2 + self.b2)

	def f(self, x):
		return self.f2(self.f1(x))

	def logits(self, x):
		return x @ self.W2 + self.b2
	
	def loss(self, x ,y):
		return torch.nn.functional.binary_cross_entropy_with_logits(self.logits(self.f1(x)), y)

model = XorOpertorNN()
optimizer = torch.optim.SGD(model.parameters(), lr=5)
print(f'W1 = {model.W1}, W2 = {model.W2}, b1 = {model.b1}, b2 = {model.b2}')
print()

for epoch in range(10000):
	model.loss(x_train, y_train).backward()
	optimizer.step()
	optimizer.zero_grad()

print(f'W1 = {model.W1}, W2 = {model.W2}, b1 = {model.b1}, b2 = {model.b2}, loss = {model.loss(x_train, y_train)}')

fig = plt.figure(figsize=(10,10))
ax = fig.add_subplot(111, projection='3d')
ax.set_xlabel("$x_1$")
ax.set_ylabel("$x_2$")
ax.set_zlabel("$y$")
ax.set_zlim(0, 1.25)

x1, x2 = torch.meshgrid(torch.linspace(1.25, 0, 100), torch.linspace(1.25, 0, 100))

y_grid = torch.empty([100,100])

for i in range(100):
	for j in range(100):
		x = torch.tensor([[x1[i,j], x2[i,j]]])
		y_grid[i,j] = model.f(x).item()

ax.plot_wireframe(x1,x2,y_grid, color='red')

plt.show()


