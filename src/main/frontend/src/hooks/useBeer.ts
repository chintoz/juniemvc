import { useState, useEffect, useCallback } from 'react';
import { beerService } from '../services/beerService';
import type { Beer, PagedBeerResponse, BeerPatch } from '../services/beerService';

interface BeerFilter {
  beerName?: string;
  beerStyle?: string;
  page?: number;
  size?: number;
  sortField?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export function useBeers(initialFilter: BeerFilter = {}) {
  const [beers, setBeers] = useState<PagedBeerResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);
  const [filter, setFilter] = useState<BeerFilter>({
    page: 0,
    size: 20,
    sortField: 'id',
    sortDirection: 'ASC',
    ...initialFilter
  });

  const fetchBeers = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await beerService.getBeers(
        filter.page ?? 0,
        filter.size ?? 20,
        filter.beerName,
        filter.beerStyle,
        filter.sortField,
        filter.sortDirection
      );
      setBeers(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    fetchBeers();
  }, [fetchBeers]);

  const updateFilter = useCallback((newFilter: Partial<BeerFilter>) => {
    setFilter((prev) => {
      // If we're changing anything other than page, reset to first page
      if (Object.keys(newFilter).some(key => key !== 'page')) {
        return { ...prev, ...newFilter, page: 0 };
      }
      return { ...prev, ...newFilter };
    });
  }, []);

  return {
    beers,
    loading,
    error,
    filter,
    updateFilter,
    refetch: fetchBeers,
  };
}

export function useBeer(beerId: number | null) {
  const [beer, setBeer] = useState<Beer | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchBeer = useCallback(async () => {
    if (beerId === null) return;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerService.getBeerById(beerId);
      setBeer(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [beerId]);

  useEffect(() => {
    fetchBeer();
  }, [fetchBeer]);

  const updateBeer = useCallback(async (updatedBeer: Omit<Beer, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
    if (!beerId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerService.updateBeer(beerId, updatedBeer);
      setBeer(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [beerId]);

  const patchBeer = useCallback(async (patch: BeerPatch) => {
    if (!beerId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerService.patchBeer(beerId, patch);
      setBeer(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [beerId]);

  const deleteBeer = useCallback(async () => {
    if (!beerId) return false;
    
    try {
      setLoading(true);
      setError(null);
      await beerService.deleteBeer(beerId);
      setBeer(null);
      return true;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return false;
    } finally {
      setLoading(false);
    }
  }, [beerId]);

  return {
    beer,
    loading,
    error,
    updateBeer,
    patchBeer,
    deleteBeer,
    refetch: fetchBeer,
  };
}