using GeneratorService.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace GeneratorService.Contexts
{
    public class GeneratorContext : DbContext
    {
        public DbSet<ParamValue> ParamValues { get; set; }

        public GeneratorContext(DbContextOptions options) : base(options)
        {
            Database.EnsureCreated();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (optionsBuilder is null)
            {
                throw new ArgumentNullException(nameof(optionsBuilder));
            }

            optionsBuilder.EnableSensitiveDataLogging();
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            if (modelBuilder is null)
            {
                throw new ArgumentNullException(nameof(modelBuilder));
            }

            modelBuilder.ApplyConfiguration(new ParamValuesConfiguration());
        }
    }

    public class ParamValuesConfiguration : IEntityTypeConfiguration<ParamValue>
    {
        public void Configure(EntityTypeBuilder<ParamValue> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("ParamValues");

            builder.HasAlternateKey(pv => pv.ParamValueUid);
            builder.HasAlternateKey(pv => new { pv.EquipmentUid, pv.ParamUid });

            builder.Property(em => em.Value).IsRequired();
        }
    }
}
