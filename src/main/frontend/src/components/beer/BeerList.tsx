import { useState } from 'react';
import { useBeers } from '../../hooks/useBeer';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../ui/select';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../ui/table';
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';

interface BeerListProps {
  onViewBeer: (beerId: number) => void;
  onEditBeer: (beerId: number) => void;
  onCreateBeer: () => void;
}

export function BeerList({ onViewBeer, onEditBeer, onCreateBeer }: BeerListProps) {
  const [nameFilter, setNameFilter] = useState('');
  const [styleFilter, setStyleFilter] = useState('ALL');
  
  const {
    beers,
    loading,
    error,
    filter,
    updateFilter,
  } = useBeers();

  const handleSearch = () => {
    updateFilter({
      beerName: nameFilter || undefined,
      beerStyle: styleFilter === 'ALL' ? undefined : styleFilter,
    });
  };

  const handleClearFilters = () => {
    setNameFilter('');
    setStyleFilter('ALL');
    updateFilter({
      beerName: undefined,
      beerStyle: undefined,
    });
  };

  const handlePageChange = (newPage: number) => {
    updateFilter({ page: newPage });
  };

  if (error) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle className="text-red-500">Error Loading Beers</CardTitle>
        </CardHeader>
        <CardContent>
          <p>{error.message}</p>
          <Button onClick={() => window.location.reload()} className="mt-4">
            Retry
          </Button>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="w-full">
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>Beers</CardTitle>
        <Button onClick={onCreateBeer}>Add New Beer</Button>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col md:flex-row gap-4 mb-6">
          <div className="flex-1">
            <Input
              placeholder="Filter by name"
              value={nameFilter}
              onChange={(e) => setNameFilter(e.target.value)}
            />
          </div>
          <div className="flex-1">
            <Select value={styleFilter} onValueChange={setStyleFilter}>
              <SelectTrigger>
                <SelectValue placeholder="Filter by style" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">All Styles</SelectItem>
                <SelectItem value="ALE">Ale</SelectItem>
                <SelectItem value="IPA">IPA</SelectItem>
                <SelectItem value="LAGER">Lager</SelectItem>
                <SelectItem value="STOUT">Stout</SelectItem>
                <SelectItem value="WHEAT">Wheat</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <Button onClick={handleSearch}>Search</Button>
          <Button variant="outline" onClick={handleClearFilters}>
            Clear
          </Button>
        </div>

        {loading ? (
          <div className="flex justify-center p-8">Loading...</div>
        ) : (
          <>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Style</TableHead>
                  <TableHead>Price</TableHead>
                  <TableHead>Quantity</TableHead>
                  <TableHead>Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {beers?.content.map((beer) => (
                  <TableRow key={beer.id}>
                    <TableCell>{beer.beerName}</TableCell>
                    <TableCell>{beer.beerStyle}</TableCell>
                    <TableCell>${beer.price.toFixed(2)}</TableCell>
                    <TableCell>{beer.quantityOnHand}</TableCell>
                    <TableCell>
                      <div className="flex gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => onViewBeer(beer.id)}
                        >
                          View
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => onEditBeer(beer.id)}
                        >
                          Edit
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>

            {beers && (
              <div className="flex justify-between items-center mt-4">
                <div>
                  Showing {beers.pageable.offset + 1} to{' '}
                  {Math.min(
                    beers.pageable.offset + beers.content.length,
                    beers.totalElements
                  )}{' '}
                  of {beers.totalElements} beers
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    disabled={filter.page === 0}
                    onClick={() => handlePageChange((filter.page ?? 0) - 1)}
                  >
                    Previous
                  </Button>
                  <Button
                    variant="outline"
                    disabled={beers.last}
                    onClick={() => handlePageChange((filter.page ?? 0) + 1)}
                  >
                    Next
                  </Button>
                </div>
              </div>
            )}
          </>
        )}
      </CardContent>
    </Card>
  );
}