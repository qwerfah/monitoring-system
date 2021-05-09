using DocumentationService.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace DocumentationService.Contexts
{
    public class DocumentationContext : DbContext
    {
        public DbSet<DocumentationFile> DocumentationFiles { get; set; }

        public DocumentationContext(DbContextOptions options) : base(options)
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

            modelBuilder.ApplyConfiguration(new DocumentationFilesConfiguration());
        }
    }

    public class DocumentationFilesConfiguration : IEntityTypeConfiguration<DocumentationFile>
    {
        public void Configure(EntityTypeBuilder<DocumentationFile> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("DocumentationFiles");

            builder.HasAlternateKey(pv => pv.FileUid);

            builder.Property(em => em.EquipmentModelUid).IsRequired();
            builder.Property(em => em.Name).IsRequired();
            builder.Property(em => em.Content).IsRequired();
        }
    }
}
