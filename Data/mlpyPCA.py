## This code is written by Davide Albanese, <albanese@fbk.eu>.
## (C) 2011 mlpy Developers.

## This program is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.

## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.

## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <http://www.gnu.org/licenses/>.

import numpy as np
import scipy.linalg as spla

__all__ = ['LDA', 'SRDA', 'KFDA', 'PCA', 'PCAFast', 'KPCA']


def proj(u, v):
    """(<v, u> / <u, u>) u
    """

    return (np.dot(v, u) / np.dot(u, u)) * u

def pca(xarr, method='svd'):
    """Principal Component Analysis.
    
    Returns the principal component coefficients `coeff`(K,K) 
    and the corresponding eigenvalues (K) of the covariance 
    matrix of `x` (N,P) sorted by decreasing eigenvalue, where 
    K=min(N,P). Each column of `x` represents a variable,  
    while the rows contain observations. Each column of `coeff` 
    contains coefficients for one principal component.
    
    Sample(s) can be embedded into the M (<=K) dimensional
    space by z = x coeff_M (z = np.dot(x, coeff[:, :M])).

    :Parameters:
       x : 2d numpy array (N, P)
          data matrix
       method : str
          'svd' or 'cov'
    
    :Returns:
       coeff, evals : 2d numpy array (K, K), 1d numpy array (K)
          principal component coefficients (eigenvectors of
          the covariance matrix of x) and eigenvalues sorted by 
          decreasing eigenvalue.
    """


    n, p = xarr.shape
    
    if method == 'svd':
        x_h = (xarr - np.mean(xarr, axis=0)) / np.sqrt(n - 1)
        u, s, v = np.linalg.svd(x_h.T, full_matrices=False)
        evecs = u
        evals = s**2
    elif method == 'cov':
        k = np.min((n, p))
        C = np.cov(xarr, rowvar=0)
        evals, evecs = np.linalg.eigh(C)
        idx = np.argsort(evals)[::-1]
        evecs = evecs[:, idx]
        evals = evals[idx]
        evecs = evecs[:, :k]
        evals = evals[:k]
    else:
        raise ValueError("method must be 'svd' or 'cov'")

    return evecs, evals

class PCA:
    """Principal Component Analysis.
    """
    
    def __init__(self, method='svd', whiten=False):
        """Initialization.
        
        :Parameters:
           method : str
              method, 'svd' or 'cov'
           whiten : bool
              whitening. The eigenvectors will be scaled
              by eigenvalues**-(1/2)
        """
        
        self._coeff = None
        self._coeff_inv = None
        self._evals = None
        self._mean = None
        self._method = method
        self._whiten = whiten        

    def learn(self, x):
        """Compute the principal component coefficients.
        `x` is a matrix (N,P). Each column of `x` represents a 
        variable, while the rows contain observations.
        """

        xarr = np.asarray(x, dtype=np.float)
        if xarr.ndim != 2:
            raise ValueError("x must be a 2d array_like object")
        
        self._mean = np.mean(xarr, axis=0)
        self._coeff, self._evals = pca(x, method=self._method)

        if self._whiten:
            self._coeff_inv = np.empty((self._coeff.shape[1], 
                self._coeff.shape[0]), dtype=np.float)
            
            for i in range(len(self._evals)):
                eval_sqrt = np.sqrt(self._evals[i])
                self._coeff_inv[i] = self._coeff[:, i] * \
                    eval_sqrt
                self._coeff[:, i] /= eval_sqrt
        else:
            self._coeff_inv = self._coeff.T

    def transform(self, t, k=None):
        """Embed `t` (M,P) into the k dimensional subspace.
        Returns a (M,K) matrix. If `k` =None will be set to 
        min(N,P)
        """

        if self._coeff is None:
            raise ValueError("no PCA computed")
        
        if k == None:
            k = self._coeff.shape[1]

        if k < 1 or k > self._coeff.shape[1]:
            raise ValueError("k must be in [1, %d] or None" % \
                                 self._coeff.shape[1])

        tarr = np.asarray(t, dtype=np.float)

        try:
            return np.dot(tarr-self._mean, self._coeff[:, :k])
        except:
            raise ValueError("t, coeff: shape mismatch")
            
    def transform_inv(self, z):
        """Transform data back to its original space,
        where `z` is a (M,K) matrix. Returns a (M,P) matrix.
        """

        if self._coeff is None:
            raise ValueError("no PCA computed")

        zarr = np.asarray(z, dtype=np.float)

        return np.dot(zarr, self._coeff_inv[:zarr.shape[1]]) +\
            self._mean
        
    def coeff(self):
        """Returns the tranformation matrix (P,L), where
        L=min(N,P), sorted by decreasing eigenvalue.
        Each column contains coefficients for one principal 
        component.
        """
        
        return self._coeff
    
    def coeff_inv(self):
        """Returns the inverse of tranformation matrix (L,P),
        where L=min(N,P), sorted by decreasing eigenvalue.
        """
        
        return self._coeff_inv

    def evals(self):
        """Returns sorted eigenvalues (L), where L=min(N,P).
        """
        
        return self._evals   