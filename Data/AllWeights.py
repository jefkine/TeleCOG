import mlpyPCA
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

all_weights = np.loadtxt('AllWeights.csv', delimiter=',')
aw = all_weights[:, :10]

pca = mlpyPCA.PCA() # new PCA instance
pca.learn(aw) # learn from data
z = pca.transform(aw, k=2) # embed aw into the k=2 dimensional subspace

x = z[:, 0]
y = z[:, 1]

# fig, ax = plt.subplots()
# im = ax.hexbin(x, y, gridsize=15)
# fig.colorbar(im, ax=ax)

fig, ax = plt.subplots()
H = ax.hist2d(x, y, bins=15)
fig.colorbar(H[3], ax=ax)

plt.show()


