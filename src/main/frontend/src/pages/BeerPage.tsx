import { useState } from 'react';
import { BeerList } from '../components/beer/BeerList';
import { BeerDetail } from '../components/beer/BeerDetail';
import { BeerForm } from '../components/beer/BeerForm';

type View = 'list' | 'detail' | 'create' | 'edit';

export function BeerPage() {
  const [currentView, setCurrentView] = useState<View>('list');
  const [selectedBeerId, setSelectedBeerId] = useState<number | null>(null);

  // Navigation handlers
  const handleViewBeer = (beerId: number) => {
    setSelectedBeerId(beerId);
    setCurrentView('detail');
  };

  const handleEditBeer = (beerId: number) => {
    setSelectedBeerId(beerId);
    setCurrentView('edit');
  };

  const handleCreateBeer = () => {
    setSelectedBeerId(null);
    setCurrentView('create');
  };

  const handleBackToList = () => {
    setCurrentView('list');
  };

  const handleSaveComplete = () => {
    setCurrentView('list');
  };

  // Render the appropriate view
  const renderView = () => {
    switch (currentView) {
      case 'detail':
        return (
          <BeerDetail
            beerId={selectedBeerId!}
            onBack={handleBackToList}
            onEdit={handleEditBeer}
          />
        );
      case 'create':
        return (
          <BeerForm
            onSave={handleSaveComplete}
            onCancel={handleBackToList}
          />
        );
      case 'edit':
        return (
          <BeerForm
            beerId={selectedBeerId!}
            onSave={handleSaveComplete}
            onCancel={handleBackToList}
          />
        );
      case 'list':
      default:
        return (
          <BeerList
            onViewBeer={handleViewBeer}
            onEditBeer={handleEditBeer}
            onCreateBeer={handleCreateBeer}
          />
        );
    }
  };

  return (
    <div className="container mx-auto py-8 px-4">
      <h1 className="text-3xl font-bold mb-8">Beer Management</h1>
      {renderView()}
    </div>
  );
}