using System.Threading.Tasks;
using Actio.Services.Activities.Domain.Repositories;
using Actio.Common.Mongo;
using Actio.Services.Activities.Domain.Models;
using MongoDB.Driver;
using MongoDB.Driver.Linq;
using System.Collections.Generic;

namespace Actio.Services.Activities.Repositories
{
    public class CategoryRepository : ICategoryRepository
    {
        private readonly IMongoDatabase _mongoDatabase;

        public CategoryRepository(IMongoDatabase database)
        {
            _mongoDatabase = database;
        }

        public async Task<Category> GetAsync(string name)
        {
            return await Collection.AsQueryable().FirstOrDefaultAsync(x=>x.Name == name.ToLowerInvariant());
        }

        public async Task AddAsync(Category category)
        {
            await Collection.InsertOneAsync(category);
        }

        public async Task<IEnumerable<Category>> BrowseAsync()
        {
            return await Collection.AsQueryable().ToListAsync();
        }

        private IMongoCollection<Category> Collection => _mongoDatabase.GetCollection<Category>("Categories");

       
    }
}