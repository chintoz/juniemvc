import { useBeer } from '../../hooks/useBeer';
import { Button } from '../ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '../ui/card';

interface BeerDetailProps {
  beerId: number;
  onBack: () => void;
  onEdit: (beerId: number) => void;
}

export function BeerDetail({ beerId, onBack, onEdit }: BeerDetailProps) {
  const { beer, loading, error, deleteBeer } = useBeer(beerId);

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this beer?')) {
      const success = await deleteBeer();
      if (success) {
        onBack();
      }
    }
  };

  if (loading) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle>Loading Beer Details...</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex justify-center p-8">Loading...</div>
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle className="text-red-500">Error Loading Beer</CardTitle>
        </CardHeader>
        <CardContent>
          <p>{error.message}</p>
          <Button onClick={onBack} className="mt-4">
            Back to List
          </Button>
        </CardContent>
      </Card>
    );
  }

  if (!beer) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle>Beer Not Found</CardTitle>
        </CardHeader>
        <CardContent>
          <p>The requested beer could not be found.</p>
          <Button onClick={onBack} className="mt-4">
            Back to List
          </Button>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>{beer.beerName}</CardTitle>
        <CardDescription>Beer Details</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <h3 className="text-lg font-semibold">Basic Information</h3>
            <div className="mt-2 space-y-2">
              <div>
                <span className="font-medium">Style:</span> {beer.beerStyle}
              </div>
              <div>
                <span className="font-medium">UPC:</span> {beer.upc}
              </div>
              <div>
                <span className="font-medium">Price:</span> ${beer.price.toFixed(2)}
              </div>
              <div>
                <span className="font-medium">Quantity On Hand:</span> {beer.quantityOnHand}
              </div>
            </div>
          </div>
          <div>
            <h3 className="text-lg font-semibold">System Information</h3>
            <div className="mt-2 space-y-2">
              <div>
                <span className="font-medium">ID:</span> {beer.id}
              </div>
              <div>
                <span className="font-medium">Version:</span> {beer.version}
              </div>
              <div>
                <span className="font-medium">Created Date:</span>{' '}
                {new Date(beer.createdDate).toLocaleString()}
              </div>
              <div>
                <span className="font-medium">Last Updated:</span>{' '}
                {new Date(beer.updateDate).toLocaleString()}
              </div>
            </div>
          </div>
        </div>
      </CardContent>
      <CardFooter className="flex justify-between">
        <Button variant="outline" onClick={onBack}>
          Back to List
        </Button>
        <div className="space-x-2">
          <Button variant="outline" onClick={() => onEdit(beer.id)}>
            Edit
          </Button>
          <Button variant="destructive" onClick={handleDelete}>
            Delete
          </Button>
        </div>
      </CardFooter>
    </Card>
  );
}